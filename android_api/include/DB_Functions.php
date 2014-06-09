<?php
 
class DB_Functions {
 
    private $db;
	
 
    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($sex, $last_name, $first_name, $pseudo, $email, $password ) {
  //      $uuid = uniqid('', true);
      // $hash = $this->hashSSHA($password);
        //$encrypted_password =  $password //$hash["encrypted"]; // encrypted password
      //  $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO users2(sex, last_name, first_name, pseudo, email, password) VALUES('$sex', '$last_name', '$first_name', '$pseudo', '$email', '$password')");
        // check for successful store
        if ($result) {
            // get user details
            $id = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM users2 WHERE id = $id");
            // return user details
            return mysql_fetch_array($result);
       } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
	
	
        $result = mysql_query("SELECT * FROM users2 WHERE email = '$email'") or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            // check for password equality
            if ($password == $password) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from users2 WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
    }
	
	/**
	* Password reset
	*/
	public function passwordReset($email) {
		$result = mysql_query("SELECT * from users2 WHERE email = '$email'");
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
			return $result;
        } else {
            // user not found
            return false;
        }
	}
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
   // public function hashSSHA($password) {
 
     //   $salt = sha1(rand());
       // $salt = substr($salt, 0, 10);
      //  $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
      //  $hash = array("salt" => $salt, "encrypted" => $encrypted);
      //  return $hash;
    //}
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
//    public function checkhashSSHA($salt, $password) {
 
  //      $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
    //    return $hash;
   // }
 
}
 
?>