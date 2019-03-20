package assignment;
/**
 *
 * @shahkar khan
 */
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;

public class Run {

   public static void main(String argv[]) throws SQLException, ClassNotFoundException {
       
       String id = null,name = null,Email = null,CountryCode = null,Batch = null,Gpa = null;
       Connection connection = null;
       Statement stmt = null;
       
       try 
       {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/check", "root", " ");
        stmt = connection.createStatement();
        File fXmlFile = new File("C:/Users/MSI/Documents/student_info.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);       
        doc.getDocumentElement().normalize(); 
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("student");
        System.out.println("----------------------------"); 
        Node nNode = nList.item(0);
        System.out.println("\nCurrent Element :" + nNode.getNodeName());
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode;         
        id=eElement.getAttribute("id");
        name=eElement.getElementsByTagName("name").item(0).getTextContent();
        Email=eElement.getElementsByTagName("Email").item(0).getTextContent();
        CountryCode=eElement.getElementsByTagName("CountryCode").item(0).getTextContent();
        Batch=eElement.getElementsByTagName("Batch").item(0).getTextContent();
        Gpa=eElement.getElementsByTagName("Gpa").item(0).getTextContent();        
        System.out.println("Student  id Fetch from Xml is : " + eElement.getAttribute("id"));
        
        stmt.execute("INSERT INTO student_data (student_Id,Name,Email,CountryCode,Batch,Gpa) VALUES ('"+id+"','"+name+"','"+Email+"','"+CountryCode+"','"+Batch+"','"+Gpa+"')");
        System.out.println("Data Inserted Successfully");
        }
       }
        catch (Exception e) {
            e.printStackTrace();
        }   
        //////////////
      	try {	   	   
			stmt = connection.createStatement();                       
			String sql = "SELECT * FROM  Student_Data ORDER BY student_Id ASC";			
			ResultSet rec = stmt.executeQuery(sql);	
                        
                        //will create new xml file having your sql data
			String strPath = "C:/Users/MSI/Documents/NewUpdated.xml";
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();	
			Document doc = documentBuilder.newDocument();			
			Element ele = doc.createElement("FAST");
			doc.appendChild(ele);
			int iRows = 0;
			while((rec!=null) && (rec.next()))
                        {   				
				++iRows;							
				Element sRows = doc.createElement("Student");
				ele.appendChild(sRows);			
				Attr sAttrID = doc.createAttribute("id");
				sAttrID.setValue(String.valueOf(iRows));
				sRows.setAttributeNode(sAttrID);
                                
				Element stname = doc.createElement("name");
				stname.appendChild(doc.createTextNode(rec.getString("Name")));
				sRows.appendChild(stname);	
                                
				Element stemail = doc.createElement("Email");
				stemail.appendChild(doc.createTextNode(rec.getString("Email")));
				sRows.appendChild(stemail);
                                
				Element stcountry = doc.createElement("CountryCode");
				stcountry.appendChild(doc.createTextNode(rec.getString("CountryCode")));
				sRows.appendChild(stcountry);
                                
				Element stbatch = doc.createElement("Batch");
				stbatch.appendChild(doc.createTextNode(rec.getString("Batch")));
				sRows.appendChild(stbatch);
                                
				Element stgpa = doc.createElement("Gpa");
				stgpa.appendChild(doc.createTextNode(rec.getString("Gpa")));
				sRows.appendChild(stgpa);				
            }	

			// creating and writing to xml file
			TransformerFactory tff = TransformerFactory
					.newInstance();
			Transformer tf = tff.newTransformer();
			DOMSource domSource = new DOMSource(doc);
			StreamResult streamResult = new StreamResult(new File(strPath));

	               // tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");

			tf.transform(domSource, streamResult);

			System.out.println("XML file created!");

		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
            try {
                stmt.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         
   }  
 }
