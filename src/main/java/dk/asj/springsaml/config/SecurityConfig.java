package dk.asj.springsaml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.servlet.filter.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2AuthenticationTokenConverter;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http, RelyingPartyRegistrationRepository registrations,
                                          AuthenticationConverter authenticationConverter,
                                          Saml2MetadataFilter saml2MetadataFilter) throws Exception {
    return http
      // basic test setup where you can access some endpoints without authentication, and some that require it
      .authorizeRequests(auth -> {
        auth.antMatchers("/").permitAll();
        auth.antMatchers("/**").authenticated();
      })

      // Default .saml2Login() sets loginProcessingUrl: /saml2/authenticate/{registrationId} to support several
      // registrations. Since we only have one and the example app was registered with the IdP using a custom url, we
      // must override the default url. This also means we must supply a custom auth converter that can convert the
      // saml request when the http request does not have a registration ID to identify the registration.
      .saml2Login(saml2 -> saml2
        .loginProcessingUrl("/login/saml2/sso")
        .authenticationConverter(authenticationConverter)
      )

      // Process registration metadata before the auth filter
      .addFilterBefore(saml2MetadataFilter, Saml2WebSsoAuthenticationFilter.class)
      .build();
  }

  /**
   * Custom SAML2 auth request converter that is hardcoded to resolve the only registration in this app (ID: metadata).
   * <p>
   * Normally the default converter will use the http request to resolve a registration ID from a URL request path
   * fragment, but since we have a custom url for processing the login request we cannot rely on the default converter.
   * @param registrations repository of the known registrations configured in this app.
   * @return the custom converter bean.
   */
  @Bean
  AuthenticationConverter saml2AuthenticationConverter(RelyingPartyRegistrationRepository registrations) {
    var resolver = new DefaultRelyingPartyRegistrationResolver(id -> registrations.findByRegistrationId("metadata"));
    return new Saml2AuthenticationTokenConverter(resolver);
  }

  /**
   * Filter to expose SAML2 registration metadata. This is done on the url
   * {@code /saml2/service-provider-metadata/{registrationId}} to facilitate several registrations in the app.
   * <p>
   * The filter uses a request's {@code registrationId} path fragment to look up the registration in the app's
   * repository of configured registrations.
   * @param registrations repository of the known registrations configured in this app.
   * @return the metadata filter bean.
   */
  @Bean
  Saml2MetadataFilter metadataFilter(RelyingPartyRegistrationRepository registrations) {
    var resolver = new DefaultRelyingPartyRegistrationResolver(registrations);
    return new Saml2MetadataFilter(resolver, new OpenSamlMetadataResolver());
  }

}
