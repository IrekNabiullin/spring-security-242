package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import web.config.handler.LoginSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private DataSource dataSource;
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("ADMIN").password("ADMIN").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("USER").password("USER").roles("USER");
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .usersByUsernameQuery("select username, password, active from users where username=?")
//                .authoritiesByUsernameQuery("select u.username, ur.roles from users u inner join user_role ur on u.id = ur.user_id where u.username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/**").authenticated()
//                .antMatchers("/admin/**").hasRole("ROLE_ADMIN")
//                .antMatchers("/profile/**").hasAnyRole("ROLE_ADMIN", "ROLE_USER")
//                .and()
//                .httpBasic()
//                .and()
//                .logout().logoutSuccessUrl("/");

        http.formLogin()
                // указываем страницу с формой логина
                .loginPage("/login")
                //указываем логику обработки при логине
                .successHandler(new LoginSuccessHandler())
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                // даем доступ к форме логина всем
                .permitAll();

        http.logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/login?logout")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and()
                .csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //страницы аутентификаци доступна всем
                .antMatchers("/login").anonymous()
                // защищенные URL
 //               .antMatchers("/users/**").hasAuthority("ADMIN")
//                .antMatchers("/admin**").hasAuthority("ADMIN")
 //               .antMatchers("/profile").hasAnyAuthority("USER", "ADMIN");
 //               .antMatchers("/users").access("hasRole('ADMIN')").anyRequest().authenticated()
  //              .antMatchers("/users").hasRole("ADMIN");
 //               .antMatchers("/users/edit").access("hasAnyRole('ADMIN')").anyRequest().authenticated();
//                .antMatchers("/admin/**").access("hasRole('ADMIN')").anyRequest().authenticated();
//                .antMatchers("/**").authenticated();
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/profile")
                    .access("hasAnyRole('ADMIN', 'USER')")
                    .anyRequest().authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
