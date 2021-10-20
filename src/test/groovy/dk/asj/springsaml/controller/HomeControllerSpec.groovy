package dk.asj.springsaml.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
class HomeControllerSpec extends Specification {

  @Autowired
  WebApplicationContext context

  MockMvc client

  def setup() {
    //client = web
    client = MockMvcBuilders.webAppContextSetup(context).build()
  }

  def 'should allow all access to /'() {
    when:
    def response = client.perform(get('/'))

    then:
    response.andExpect(status().isOk())
    response.andExpect(content().string('hello from unrestricted endpoint'))
  }

  def 'should reject unauthenticated access to /secret'() {
    expect:
    client
      .perform(get('/secret'))
      .andExpect(status().isUnauthorized())
  }

  def 'should allow authenticated access to /secret'() {
    expect:
    client
      .perform(get('/secret'))
      .andExpect(status().isOk())
  }
}
