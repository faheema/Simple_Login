<?php
require_once 'utils/db_apis.php';
$db = new DBApis();
 
// json response array
$response = array();
 
if (isset($_POST['email']) && isset($_POST['password'])) {
 
    // receiving the post params
    /* @var $_POST type */
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    // get the user by email and password
    $user = $db->getUserByEmailAndPassword($email, $password);
 
    if ($user) {
        // use is found
        $response["success"] = TRUE;
        $response["user"]["name"] = $user["name"];
        $response["user"]["email"] = $user["email"];
        $response["user"]["contact"] = $user["contact"];
      
        echo json_encode($response);
    } else {
        // user is not found with the credentials
        $response["success"] = FALSE;
        $response["msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["sucess"] = FALSE;
    $response["msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}
