package ru.tatarinov.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tatarinov.banking.services.ClientDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    private final ClientDetailsService clientDetailsService;

    public SecurityConfig(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    // настраиваем аутентификацию
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(clientDetailsService);
        }

    // указываем, что пароль не шифруем (пока)
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
        //return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()											//отключаем защту от межсайтовой подделки запросов (временно)
                .authorizeRequests()										//авторизация
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()		//пускать неавторизованных на страницу логина и страницу ошибки
                .antMatchers("/clients/default").hasAnyRole("ADMIN", "USER")
                .antMatchers("/clients/{userId}").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/cards/new/{userId}").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/cards/{cardId}/**").access("@webSecurity.checkCardId(authentication,#cardId)")
                .anyRequest().authenticated()							//для всех остальных запросов должен быть аутентифицирован
            .and()
                .formLogin().loginPage("/auth/login")					//настройка страницы авторизации
                .loginProcessingUrl("/process_login")					//адрес, на котором будет обрабатываться авторизация. Может быть любым, но должны передаваться именно username и password
                .defaultSuccessUrl("/clients/default", true)
                .failureUrl("/auth/login?error")
            .and()
                .exceptionHandling().accessDeniedPage("/auth/notFound")
            .and()
                .logout()												//настройка страницы логаута
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login")
        ;
    }
}
