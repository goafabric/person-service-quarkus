package org.goafabric.personservice.architecture

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import com.tngtech.archunit.library.Architectures
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import org.goafabric.personservice.Application


@AnalyzeClasses(packagesOf = [Application::class], importOptions = [DoNotIncludeTests::class])
internal object ControllerRulesTest {
    @ArchTest
    val layerAreRespectedBasic: ArchRule = Architectures.layeredArchitecture()
        .consideringOnlyDependenciesInLayers()

        .layer("Controller").definedBy("..controller")
        .layer("Logic").definedBy("..logic..")

        .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
        .whereLayer("Logic").mayOnlyBeAccessedByLayers("Controller")

    @ArchTest
    val controllerNaming: ArchRule = ArchRuleDefinition.classes()
        .that().areAnnotatedWith(Produces::class.java)
        .should().haveSimpleNameEndingWith("Controller")

    /*
    @ArchTest
    val controllerShouldNotUseCrossOrigin: ArchRule = ArchRuleDefinition.classes()
        .should().notBeAnnotatedWith(CrossOrigin::class.java)

    @ArchTest
    val controllerShouldNotReturnResponseEntity: ArchRule = ArchRuleDefinition.methods()
        .that().areDeclaredInClassesThat().areAnnotatedWith(RestController::class.java)
        .should(object : ArchCondition<JavaMethod>("ResponseEntity check") {
            override fun check(method: JavaMethod, events: ConditionEvents) {
                if (method.rawReturnType.isAssignableFrom(ResponseEntity::class.java)) {
                    events.add(
                        SimpleConditionEvent.violated(
                            method,
                            String.format("Method %s in %s returns ResponseEntity", method.name, method.owner.name)
                        )
                    )
                }
            }
        })

     */
}
