Feature: Validating Place APIs
    @AddPlace
Scenario Outline: Verify place is added successfully using AddPlaceAPI
    Given Add Place Payload with "<name>" "<language>" "<address>" "<phone>" "<website>"
    When User calls "AddPlaceAPI" with "POST" http request
    Then API call got success with status code 200
    And "status" in response body is "OK"
    And "scope" in response body is "APP"
    And verify place_id created maps to "<name>" using "GetPlaceAPI"

    Examples:
        | name      | language | address          | phone          | website      |
#        | My Home   | English  | 123, Main Street | +91 9784405656 | yahoo.com    |
#        | your home | Hindi    | 456, Park Avenue | +91 9876543210 | googley.com  |
#        | Our Home  | Spanish  | 789, Broadway    | +91 9123456789 | facebook.com |
        | All Home  | French   | 321, Elm Street  | +91 9876543210 | twitter.com  |

        @DeletePlace
Scenario: Verify if DeletePlaceAPI is working as expected
    Given DeletePlace Payload
    When User calls "DeletePlaceAPI" with "POST" http request
    Then API call got success with status code 200
    And "status" in response body is "OK"




