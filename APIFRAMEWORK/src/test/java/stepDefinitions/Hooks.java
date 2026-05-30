package stepDefinitions;


import io.cucumber.java.Before;

import java.io.IOException;

public class Hooks {
    @Before("@DeletePlace")
    public void beforeScenario() throws IOException {
        // weite a code which will give you place id
        // Execute the code only when place_id is Null

        StepDefinitions m = new StepDefinitions();
        if (StepDefinitions.place_id==null) {
            m.addPlacePayloadWith("Sheela", "Hindi", "Noida Sector 62", "9876543210", "http://google.com");
            m.userCallsWithHttpRequest("AddPlaceAPI", "POST");
            m.verify_place_id_created_maps_to_using("Sheela", "GetPlaceAPI");
        }




    }
}
