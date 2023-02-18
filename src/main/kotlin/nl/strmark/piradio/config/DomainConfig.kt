package nl.strmark.piradio.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EntityScan("nl.strmark.piradio.domain")
@EnableJpaRepositories("nl.strmark.piradio.repos")
@EnableTransactionManagement
class DomainConfig
