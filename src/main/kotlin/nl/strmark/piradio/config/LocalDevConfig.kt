package nl.strmark.piradio.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.FileTemplateResolver
import java.io.File

@Configuration
@Profile("local")
class LocalDevConfig(
    templateEngine: TemplateEngine
) {

    init {
        var sourceRoot: File = ClassPathResource("application.yml").file.parentFile
        while ((sourceRoot.listFiles { _, name -> name == ("gradlew") }?.size ?: 1) != 1) {
            sourceRoot = sourceRoot.parentFile
        }
        val fileTemplateResolver = FileTemplateResolver()
        fileTemplateResolver.prefix = sourceRoot.path + "/src/main/resources/templates/"
        fileTemplateResolver.suffix = ".html"
        fileTemplateResolver.isCacheable = false
        fileTemplateResolver.characterEncoding = "UTF-8"
        fileTemplateResolver.checkExistence = true

        templateEngine.setTemplateResolver(fileTemplateResolver)
    }
}
