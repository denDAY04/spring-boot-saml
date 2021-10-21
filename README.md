# SAML Spring-boot PoC
PoC of using SAML in a spring-boot app. 

This implementation is inspired by the [official spring example](https://github.com/spring-projects/spring-security-samples/tree/main/servlet/spring-boot/java/saml2/login-single-tenant) but does so using Spring-Security 5.5, where the official example use 5.6, since 5.6 is not yet officially released as of Oct. 2021.

This app re-uses the identity provider (IdP, also known as the asserting party) and the registration from the official example, and thus this app is setup with a custom sso login endpoint in order to match the expected URLs from the perspective of the IdP. 

Useful links: 

- https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-saml2
- https://www.oasis-open.org/committees/download.php/35389/sstc-saml-profiles-errata-2.0-wd-06-diff.pdf#page=15
