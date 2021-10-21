
//
// Built on Thu Oct 21 13:07:22 CEST 2021 by dk.asj.springsaml.resources.logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the dk.asj.springsaml.resources.logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder

appender('Console', ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = '%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n'
  }
}

root(INFO, ['Console'])
logger('dk.asj.springsaml', DEBUG)
