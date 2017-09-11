package application;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TermsConditionsController {
	
	@FXML
	private JFXTextField termsConditionsText;
	
	private final String TERMS_CONDITIONS = "TERMS AND CONDITIONS\n\n 1. Introduction\n\n These Website Standard Terms and Conditions written on this webpage shall manage your use of this website. These Terms will be applied fully and affect to your use of this Website. By using this Website, you agreed to accept all terms and conditions written in here. You must not use this Website if you disagree with any of these Website Standard Terms and Conditions.\n\n Minors or people below 18 years old are not allowed to use this Website.\n\n 2. Intellectual Property Rights\n\n Other than the content you own, under these Terms, SafeDrivingSimulator and/or its licensors own all the intellectual property rights and materials contained in this Website.\n\n You are granted limited license only for purposes of viewing the material contained on this Website.\n\n 3. Restrictions\n\n You are specifically restricted from all of the following\n\n - publishing any Website material in any other media;\n - selling, sublicensing and/or otherwise commercializing any Website material;\n - publicly performing and/or showing any Website material;\n - using this Website in any way that is or may be damaging to this Website;\n - using this Website in any way that impacts user access to this Website;\n - using this Website contrary to applicable laws and regulations, or in any way may cause harm to the Website, or to any person or business entity;\n - engaging in any data mining, data harvesting, data extracting or any other similar activity in relation to this Website;\n - using this Website to engage in any advertising or marketing.\n\n Certain areas of this Website are restricted from being access by you and SafeDrivingSimulator may further restrict access by you to any areas of this Website, at any time, in absolute discretion. Any user ID and password you may have for this Website are confidential and you must maintain confidentiality as well.\n\n 4. Your Content\n\n In these Website Standard Terms and Conditions, â€œYour Contentâ€? shall mean any audio, video text, images or other material you choose to display on this Website. By displaying Your Content, you grant SafeDrivingSimulator a non-exclusive, worldwide irrevocable, sub licensable license to use, reproduce, adapt, publish, translate and distribute it in any and all media.\n\n Your Content must be your own and must not be invading any third-partyâ€™s rights. SafeDrivingSimulator reserves the right to remove any of Your Content from this Website at any time without notice.\n\n 5. No warranties\n\n This Website is provided â€œas is,â€? with all faults, and SafeDrivingSimulator express no representations or warranties, of any kind related to this Website or the materials contained on this Website. Also, nothing contained on this Website shall be interpreted as advising you.\n\n 6. Limitation of liability\n\n In no event shall SafeDrivingSimulator, nor any of its officers, directors and employees, shall be held liable for anything arising out of or in any way connected with your use of this Website whether such liability is under contract. SafeDrivingSimulator, including its officers, directors and employees shall not be held liable for any indirect, consequential or special liability arising out of or in any way related to your use of this Website.\n\n 7. Indemnification\n\n You hereby indemnify to the fullest extent SafeDrivingSimulator from and against any and/or all liabilities, costs, demands, causes of action, damages and expenses arising in any way related to your breach of any of the provisions of these Terms.\n\n 8. Severability\n\n If any provision of these Terms is found to be invalid under any applicable law, such provisions shall be deleted without affecting the remaining provisions herein.\n\n 9. Variation of Terms\n\n SafeDrivingSimulator is permitted to revise these Terms at any time as it sees fit, and by using this Website you are expected to review these Terms on a regular basis.\n\n 10. Assignment\n\n The SafeDrivingSimulator is allowed to assign, transfer, and subcontract its rights and/or obligations under these Terms without any notification. However, you are not allowed to assign, transfer, or subcontract any of your rights and/or obligations under these Terms.\n\n 11. Entire Agreement\n\n These Terms constitute the entire agreement between SafeDrivingSimulator and you in relation to your use of this Website, and supersede all prior agreements and understandings.\n\n 12. Governing Law & Jurisdiction\n\n These Terms will be governed by and interpreted in accordance with the laws of Australia, and you submit to the non-exclusive jurisdiction of the state and federal courts located in Australia for the resolution of any disputes.\n\n";
	
	public void start(Stage stage) throws Exception {
		stage.setTitle("Safe Driving Simulator");
		
		Parent root = FXMLLoader.load(getClass().getResource("TermsConditions.fxml"));
		Scene scene = new Scene(root);
		
		init();
		
		stage.setScene(scene);
		stage.show();
	}

    public void run() {
    	init();
        
    }
	
	private void init() {
		termsConditionsText.setText(TERMS_CONDITIONS);
		
	}
}
