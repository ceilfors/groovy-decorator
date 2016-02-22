package com.ceilfors.transform.gq.ast

import com.ceilfors.transform.gq.GqUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class GqTest extends Specification {

    @Rule
    TemporaryFolder temporaryFolder

    def setup() {
        System.setProperty(GqUtils.TEMP_DIR, temporaryFolder.newFolder().absolutePath)
    }

    def cleanup() {
        System.clearProperty(GqUtils.TEMP_DIR)
    }

    def <T> T newExample(Class<T> clasz) {
        def file = new File("src/test/groovy/${clasz.package.name.replace('.', '/')}/${clasz.simpleName}.groovy")
        assert file.exists()

        GroovyClassLoader invoker = new GroovyClassLoader()
        def clazz = invoker.parseClass(file)
        return clazz.newInstance() as T
    }

    def "Should write name of method with empty parameter"() {
        setup:
        def example = newExample(GqExample)

        when:
        def result = example."return 5"()

        then:
        result == 5
        GqUtils.gqFile.readLines().first().contains("return 5()")
    }

    def "Should write returned value"() {
        setup:
        def example = newExample(GqExample)

        when:
        def result = example."return 5"()

        then:
        result == 5
        GqUtils.gqFile.readLines().last().contains("-> 5")
    }

    def "Should write expression statement and the evaluated expression"() {
        setup:
        def example = newExample(GqSupportExample)
        println example.class.methods

        when:
        def result = example."3 plus 5"()

        then:
        result == 8
        GqUtils.gqFile.text == ("3 + 5=8\n")
    }


    // --- Kludge
    // Groovy doc recommends CompileStatic for GqTransformation to make compilation quicker
    // Rename GqTransformation to GqASTTransformation to follow standard
    // Remove ast package as it's a useless layer.
    // Use MethodClosure syntax to have better IDE support `GqUtils.&printToFile as MethodClosure`
    // Merge GqSupport and @Gq so that it looks like Gq.gq and @Gq.T

    // --- Feature
    // Support resolving method argument value
    // @Gq introduces result variable which is a pretty common variable name. Make it unique.

}
