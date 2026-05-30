package cucumber.Options;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import resources.Utils;
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/features/placeValidations.feature",
        plugin = "json:target/jsonReports/cucumber-report.json",
        glue = {"stepDefinitions"}
       // tags = "@DeletePlace"
)
public class TestRunner {

}
