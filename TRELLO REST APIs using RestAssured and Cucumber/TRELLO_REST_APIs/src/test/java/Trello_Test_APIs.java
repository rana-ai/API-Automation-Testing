import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

public class Trello_Test_APIs {

    String orgId;
    String memberId;
    String boardId;
    String listId;
  public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }



@Test (priority = 0)
    public void createOrg_Test1() throws InterruptedException {

        String displayName = "RanaOrg";
        String BaseURL = "https://api.trello.com/1/organizations";

        RestAssured.baseURI = BaseURL;
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.queryParam("displayName", displayName);
        requestSpecification.queryParam("key", Authentication.apiKey);
        requestSpecification.queryParam("token", Authentication.apiToken);


        Response response = requestSpecification.post();
        response.prettyPrint();

        Thread.sleep(2000);

        JsonPath pathOrg = response.jsonPath();
        orgId = pathOrg.getString("id");
        System.out.println(orgId);
        setOrgId(orgId);


    }

    @Test (priority = 1)
    public void getMemberId_Test2() throws InterruptedException {
            Response response = RestAssured
                    .given()
                    .baseUri("https://api.trello.com")
                    .basePath("/1/members/me")
                    .queryParam("key",Authentication.apiKey)
                    .queryParam("token",Authentication.apiToken)
                    .header("Content-Type","application/json")
                    .when()
                    .get();
            response.prettyPrint();

                    Thread.sleep(2000);

                    JsonPath pathMember = response.jsonPath();
            memberId = pathMember.getString("id");
            System.out.println(memberId);
            setMemberId(memberId);


    }

    @Test (priority = 2)
    public void getOrgMember_Test3(){
        Response response = RestAssured
                .given()
                .baseUri("https://api.trello.com/1/members/"+memberId+"/organizations")
                .queryParam("key",Authentication.apiKey)
                .queryParam("token",Authentication.apiToken)
                .header("Content-Type","application/json")
                .when()
                .get();
        response.prettyPrint();

    }

    @Test (priority = 3)
    public void createBoard_Test4() throws InterruptedException {
        Response response = RestAssured
                .given()
                .baseUri("https://api.trello.com/1/boards/")
                .queryParam("name","RanaBoard")
                .queryParam("key",Authentication.apiKey)
                .queryParam("token",Authentication.apiToken)
                .header("Content-Type","application/json")
                .when()
                .post();
        response.prettyPrint();

        JsonPath boardPath = response.jsonPath();
        boardId = boardPath.getString("id");
        System.out.println(boardId);

        Thread.sleep(2000);

        JsonPath pathMember = response.jsonPath();
        boardId = pathMember.getString("id");
        System.out.println(boardId);
        setBoardId(boardId);


    }

    @Test (priority = 4)
    public void getBoardInOrg_Test5(){
        Response response = RestAssured
                .given()
                .baseUri("https://api.trello.com")
                .basePath("1/organizations/"+orgId+"/boards")
                .queryParam("key",Authentication.apiKey)
                .queryParam("token",Authentication.apiToken)
                .header("Content-Type","application/json")
                .when()
                .get();
        response.prettyPrint();

    }

    @Test (priority = 5)
    public void createNewList_Test6() throws InterruptedException {
        Response response = RestAssured
                .given()
                .baseUri("https://api.trello.com/1/lists")
                .queryParam("name","RanaList")
                .queryParam("idBoard",boardId)
                .queryParam("key",Authentication.apiKey)
                .queryParam("token",Authentication.apiToken)
                .header("Content-Type","application/json")
                .when()
                .post();
        response.prettyPrint();

        Thread.sleep(2000);

        JsonPath boardPath = response.jsonPath();
        listId = boardPath.getString("id");
        System.out.println(listId);
        setListId(listId);

    }
@Test (priority = 6)
    public void get_lists_On_A_Board_Test7(){

    Response response = RestAssured
            .given()
            .baseUri("https://api.trello.com/1/boards/"+boardId+"/lists")
            .queryParam("key",Authentication.apiKey)
            .queryParam("token",Authentication.apiToken)
            .header("Content-Type","application/json")
            .when()
            .get();
    response.prettyPrint();
    }
    @Test (priority = 7)
    public void archiveList(){

        Response response = RestAssured
                .given()
                .baseUri("https://api.trello.com/1/lists/"+listId+"/closed")
                .queryParam("key",Authentication.apiKey)
                .queryParam("token",Authentication.apiToken)
                .queryParam("value","true")
                .header("Content-Type","application/json")
                .when()
                .put();
        response.prettyPrint();
    }
    @Test (priority = 8)
    public void deleteBoard (){

        Response response = RestAssured
                .given()
                .baseUri("https://api.trello.com/1/boards/"+boardId)
                .queryParam("key",Authentication.apiKey)
                .queryParam("token",Authentication.apiToken)
                .header("Content-Type","application/json")
                .when()
                .delete();
        response.prettyPrint();
    }

    @Test (priority = 9)
    public void deleteOrganization (){

        Response response = RestAssured
                .given()
                .baseUri("https://api.trello.com/1/organizations/"+orgId)
                .queryParam("key",Authentication.apiKey)
                .queryParam("token",Authentication.apiToken)
                .header("Content-Type","application/json")
                .when()
                .delete();
        response.prettyPrint();
    }
}
