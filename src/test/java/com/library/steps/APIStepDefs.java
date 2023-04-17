package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class APIStepDefs {
    LoginPage loginPage=new LoginPage();
    BookPage bookPage=new BookPage();

    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;
    /**
     * US 01 RELATED STEPS
     *
     */
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {

        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {
        givenPart.accept(contentType);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint).prettyPeek();
        thenPart = response.then();
    }
    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        thenPart.statusCode(statusCode);
    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        thenPart.contentType(contentType);
    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        thenPart.body(path, everyItem(notNullValue()));
    }
String ID;
    @And("Path param is {string}")
    public void pathParamIs(String id) {
        ID=id;
        givenPart.pathParam("id", id);

    }

    @And("{string} field should be same with path param")
    public void fieldShouldBeSameWithPathParam(String id) {
        thenPart.body("id", is(ID));
    }

    @And("following fields should not be null")
    public void followingFieldsShouldNotBeNull(List<String>allFields) {
        for (String allField : allFields) {
            thenPart.body(allField, everyItem(notNullValue()));
        }

    }

    @And("Request Content Type header is {string}")
    public void requestContentTypeHeaderIs(String header) {
        givenPart.contentType(header);
    }

    @And("I create a random {string} as request body")
    public void iCreateARandomAsRequestBody(String book) {
        givenPart.body( LibraryAPI_Util.getRandomBookMap());
    }

    @When("I send POST request to {string} endpoint")
    public void iSendPOSTRequestToEndpoint(String endpoint) {
        givenPart.post(endpoint);
    }

    @And("the field value for {string} path should be equal to {string}")
    public void theFieldValueForPathShouldBeEqualTo(String message, String sentence) {
        thenPart.body(message, is(sentence));
    }

    @And("I logged in Library UI as {string}")
    public void iLoggedInLibraryUIAs(String userType) {
        loginPage.login(userType);

    }

    @And("I navigate to {string} page")
    public void iNavigateToPage(String booksPage) {
        Assert.assertEquals(booksPage, bookPage.booksBtn.getText());
    bookPage.booksBtn.click();
    }

    @And("UI, Database and API created book information must match")
    public void uiDatabaseAndAPICreatedBookInformationMustMatch() {
    }
}
