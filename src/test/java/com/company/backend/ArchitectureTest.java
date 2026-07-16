package com.company.backend;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

@AnalyzeClasses(packages = "com.company.backend")
class ArchitectureTest {

    private static final String DOMAIN = "com.company.backend.shared.domain..";
    private static final String ADAPTERS = "com.company.backend.shared.adapters..";
    private static final String CONFIGURATION = "com.company.backend.configuration..";

    @ArchTest
    static final ArchRule domain_is_framework_free = noClasses().that().resideInAnyPackage(DOMAIN, "com.company.backend.carfleetrequests.domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..", "jakarta.persistence..", "jakarta.validation..",
                        ADAPTERS, CONFIGURATION)
                ;

    @ArchTest
    static final ArchRule application_does_not_depend_on_adapters_or_configuration = noClasses()
                .that().resideInAnyPackage("com.company.backend.shared.application..", "com.company.backend.carfleetrequests.application..")
                .should().dependOnClassesThat().resideInAnyPackage(ADAPTERS, CONFIGURATION)
                ;
}
