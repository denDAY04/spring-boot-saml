package dk.asj.springsaml.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@SpringBootTest
class HomeControllerSpec extends Specification {

  @Autowired
  ApplicationContext context

  WebTestClient client

  def setup() {
    assert context != null

    // Bind the client to the context, which ensures that Spring boot has detected the web and security config
    // If we only bind to a controller it cannot test security config, only simple method contractual behaviour
    client = WebTestClient
      .bindToApplicationContext(context)
      .build()
  }

  def 'should allow all access to /'() {
    when:
    def response = client.get().uri('/').exchange()

    then:
    response.expectStatus().isOk()
    response.expectBody(String.class).returnResult().responseBody == 'hello from unrestricted endpoint'
  }

  def 'should reject unauthenticated access to /secret'() {
    expect:
    client
      .get().uri('/secret')
      .exchange()
      .expectStatus().isUnauthorized()
  }

  def 'should allow authenticated access to /secret'() {
    expect:
    client
      .get().uri('/secret')
      .exchange()
      .expectStatus().isOk()
  }
}
