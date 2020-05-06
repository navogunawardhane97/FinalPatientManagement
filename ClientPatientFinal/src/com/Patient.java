package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Patient {

	//A common method to connect to the DB 
		private Connection connect() {
			Connection con = null;
			
			try {
				 Class.forName("com.mysql.jdbc.Driver");
				 //Provide the correct details: DBServer/DBName, username, password 
				 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3308/patientnew?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");

				//For testing          
				 System.out.print("Successfully connected");
				 
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return con; 
		}
		
		public String readPatient() {  
			String output = "";  
			
			try {  
				Connection con = connect();  
				if (con == null)  {   
					return "Error while connecting to the database for reading.";  
				} 

				// Prepare the html table to be displayed   
				output = "<table border='1'><tr><th>NIC</th>"
						+ "<th>Name</th><th>Age</th>"
						+ "<th>Addressr</th>"
						+ "<th>PhoneNo</th>"
						+ "<th>Update</th><th>Remove</th></tr>";


				  String query = "select * from patients";   
				  Statement stmt = con.createStatement();   
				  ResultSet rs = stmt.executeQuery(query); 

				  // iterate through the rows in the result set   
				  while (rs.next())   {  

					  String patientID = Integer.toString(rs.getInt("patientID"));
					  String patientNIC = rs.getString("patientNIC");
					  String patientName = rs.getString("patientName");
					  String patientAge = Integer.toString(rs.getInt("patientAge"));
					  String patientAddress = rs.getString("patientAddress");
					  String patientPhoneNo = Integer.toString(rs.getInt("patientPhoneNo"));
					 
					  // Add into the html table    

					  output += "<tr><td><input id='hidPIDUpdate' name='hidPIDUpdate' type='hidden' value='" + patientID + "'>" + patientNIC + "</td>"; 

					  output += "<td>" + patientName + "</td>";
					  output += "<td>" + patientAge + "</td>";    
					  output += "<td>" + patientAddress + "</td>"; 
					  output += "<td>" + patientPhoneNo + "</td>";    
					  
					  
					  
					// buttons     
					  output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"
					  		+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-pid='"+ patientID +"'>"+"</td></tr>";

					} 
				  
				  con.close(); 

				  // Complete the html table   
				  output += "</table>"; 
				}
				catch (Exception e) {  
					output = "Error while reading the patient.";  
					System.err.println(e.getMessage()); 
				}

				return output;
			}
		
		//Insert Patient
		public String insertPatient(String nic, String name, String age, String address, String phoneNo ) {
			String output = "";

			try {
				Connection con = connect();  

				if (con == null) {
					return "Error while connecting to the database";
				}

				// create a prepared statement   
				String query = " insert into patients (`patientID`,`patientNIC`,`patientName`,`patientAge`,`patientAddress`, `patientPhoneNo`)"+" values (?, ?, ?, ?, ?, ?)";

				PreparedStatement preparedStmt = con.prepareStatement(query);

				// binding values 
				preparedStmt.setInt(1, 0);
				preparedStmt.setString(2, nic);
				preparedStmt.setString(3, name);
				preparedStmt.setString(4, age);
				preparedStmt.setString(5, address);
				preparedStmt.setString(6, phoneNo);
				
				
				//execute the statement   
				preparedStmt.execute();   
				con.close(); 

				//Create JSON Object to show successful msg.
				String newPatient = readPatient();
				output = "{\"status\":\"success\", \"data\": \"" + newPatient + "\"}";
			}
			catch (Exception e) {  
				//Create JSON Object to show Error msg.
				output = "{\"status\":\"error\", \"data\": \"Error while Inserting patient.\"}";   
				System.err.println(e.getMessage());  
			} 

			 return output; 
		}
		
		//Update patient
		public String updatePatient(String ID, String nic, String name, String age, String address, String phoneNo)  {   
			String output = ""; 
		 
		  try   {   
			  Connection con = connect();
		 
			  if (con == null)    {
				  return "Error while connecting to the database for updating."; 
			  } 
		 
		   // create a prepared statement    
			   String query = "UPDATE patients SET patientNIC=?,patientName=?,patientAge=?,patientAddress=?,patientPhoneNo=? WHERE patientID=?";
				 
		   PreparedStatement preparedStmt = con.prepareStatement(query); 
		 
		   // binding values    
		   preparedStmt.setString(1, nic);    
		   preparedStmt.setString(2, name);    
		   preparedStmt.setString(3, age);
		   preparedStmt.setString(4, address);
		   preparedStmt.setInt(5, Integer.parseInt(phoneNo));
		   preparedStmt.setInt(6, Integer.parseInt(ID));
		   
		 
		   // execute the statement    
		   preparedStmt.execute();    
		   con.close(); 
		 
		   //create JSON object to show successful msg
		   String newPatient = readPatient();
		   output = "{\"status\":\"success\", \"data\": \"" + newPatient + "\"}";
		   }   catch (Exception e)   {    
			   output = "{\"status\":\"error\", \"data\": \"Error while Updating patient Details.\"}";      
			   System.err.println(e.getMessage());   
		   } 
		 
		  return output;  
		  }
		
		public String deletePatient(String patientID) {  
			String output = ""; 
		 
		 try  {   
			 Connection con = connect();
		 
		  if (con == null)   {    
			  return "Error while connecting to the database for deleting.";   
		  } 
		 
		  // create a prepared statement   
		  String query = "DELETE FROM patients WHERE patientID=?"; 
		
		 PreparedStatement preparedStmt  = con.prepareStatement(query) ;
		 
		  // binding values   
		  preparedStmt.setInt(1, Integer.parseInt(patientID));       
		  // execute the statement
		  preparedStmt.execute(); 
		  con.close(); 
		 
		  //create JSON Object
		  String newPatient = readPatient();
		  output = "{\"status\":\"success\", \"data\": \"" + newPatient + "\"}";
		  }  catch (Exception e)  {  
			  //Create JSON object 
			  output = "{\"status\":\"error\", \"data\": \"Error while Deleting patient.\"}";
			  System.err.println(e.getMessage());  
			  
		 } 
		 
		 return output; 
		 }
}
