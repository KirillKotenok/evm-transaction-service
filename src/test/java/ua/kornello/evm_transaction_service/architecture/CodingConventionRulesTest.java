package ua.kornello.evm_transaction_service.architecture;

import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import ua.kornello.evm_transaction_service.service.TransactionProducerService;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.INTERFACES;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.conditions.ArchConditions.accessTargetWhere;
import static com.tngtech.archunit.lang.conditions.ArchConditions.dependOnClassesThat;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.testClassesShouldResideInTheSamePackageAsImplementation;

@AnalyzeClasses(packages = "ua.kornello.evm_transaction_service",
        importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class,
                ImportOption.DoNotIncludeArchives.class}
)
public class CodingConventionRulesTest {
    @ArchTest
    ArchRule generic_exceptions_are_forbidden = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    ArchRule java_util_logging_is_forbidden = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    ArchRule field_injection_should_not_be_used = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    ArchRule standard_output_streams_should_not_be_used = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    ArchRule deprecated_api_should_not_be_used = noClasses()
            .should(accessTargetWhere(JavaAccess.Predicates.target(annotatedWith(Deprecated.class))).as("access @Deprecated members"))
            .orShould(dependOnClassesThat(annotatedWith(Deprecated.class)).as("depend on @Deprecated classes"))
            /*.andShould().notBe(Order.class)*/
            .because("there should be a better alternative");

    @ArchTest
    ArchRule test_classes_should_be_located_in_the_same_package = testClassesShouldResideInTheSamePackageAsImplementation();

    @ArchTest
    ArchRule controllers_should_not_depend_on_each_other =
            classes().that().areAnnotatedWith(RestController.class)
                    .should().onlyDependOnClassesThat().areNotAnnotatedWith(RestController.class);


    @ArchTest
    ArchRule rest_controllers_are_stateless_and_depend_on_interfaces =
            fields().that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
                    .should().beFinal()
                    .andShould().bePrivate()
                    .andShould().haveRawType(INTERFACES);

    @ArchTest
    ArchRule controllers_are_stateless_and_depend_on_interfaces =
            fields().that().areDeclaredInClassesThat().areAnnotatedWith(Controller.class)
                    .should().beFinal()
                    .andShould().bePrivate()
                    .andShould().haveRawType(INTERFACES)
                    .allowEmptyShould(true);

}