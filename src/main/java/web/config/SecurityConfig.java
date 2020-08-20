package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import web.config.handler.LoginSuccessHandler;
import web.model.User;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //    @Autowired
//    private DataSource dataSource;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("ADMIN").password("ADMIN").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("USER").password("USER").roles("USER");
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .usersByUsernameQuery("select username, password, active from users where username=?")
//                .authoritiesByUsernameQuery("select u.username, ur.roles from users u inner join user_role ur on u.id = ur.user_id where u.username=?");

//        @Service
//        public class MyUserDetailsService implements UserDetailsService {
//
//            private Map<String, User> roles = new HashMap<>();
//
//            @PostConstruct
//            public void init() {
//                roles.put("admin2", new User("admin", "{noop}admin1", getAuthority("ROLE_ADMIN")));
//                roles.put("user2", new User("user", "{noop}user1", getAuthority("ROLE_USER")));
//            }
//
//            @Override
//            public UserDetails loadUserByUsername(String username) {
//                return roles.get(username);
//            }
//
//            private List<GrantedAuthority> getAuthority(String role) {
//                return Collections.singletonList(new SimpleGrantedAuthority(role));
//            }
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
//                .successHandler(new LoginSuccessHandler())
                .successHandler(loginSuccessHandler)
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // указываем домашнюю страницу по умолчанию
//                .defaultSuccessUrl("/profile.html", true)
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
