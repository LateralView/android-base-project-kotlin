package ${packageName}

import ${applicationPackage}.RxTrampolineSchedulerRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ${className}PresenterTest {

    @get:Rule val testSchedulerRule = RxTrampolineSchedulerRule()

    @Mock lateinit var view: ${className}Contract.View
    @InjectMocks lateinit var presenter: ${className}Presenter

    @Test
    fun itShouldTestSomething() {
    }
}
