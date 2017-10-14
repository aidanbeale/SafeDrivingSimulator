/**
 * Copyright 2017 John Humphrys
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Used to control the terms and conditions view
 *
 * @author John Humphrys
 *
 */
public class TermsConditionsController {

    @FXML
    private JFXTextArea termsCondTextArea;
    @FXML
    private JFXButton acceptButton;
    private String terms = "Terms and Conditions (\"Terms\")  \n" + "==============================\n" + "\n"
            + "Last updated: September 13, 2017\n" + "\n"
            + "Please read these Terms and Conditions (\"Terms\", \"Terms and Conditions\")\n"
            + "carefully before using the http://www.safedrivingsimulator.com website (the\n"
            + "\"Service\") operated by Safe Driving Simulator (\"us\", \"we\", or \"our\").\n" + "\n"
            + "Your access to and use of the Service is conditioned on your acceptance of and\n"
            + "compliance with these Terms. These Terms apply to all visitors, users and\n"
            + "others who access or use the Service.\n" + "\n"
            + "By accessing or using the Service you agree to be bound by these Terms. If you\n"
            + "disagree with any part of the terms then you may not access the Service. Terms\n"
            + "& Conditions created by [TermsFeed](https://termsfeed.com) for Safe Driving\n" + "Simulator.\n" + "\n"
            + "Links To Other Web Sites  \n" + "------------------------\n" + "\n"
            + "Our Service may contain links to third-party web sites or services that are\n"
            + "not owned or controlled by Safe Driving Simulator.\n" + "\n"
            + "Safe Driving Simulator has no control over, and assumes no responsibility for,\n"
            + "the content, privacy policies, or practices of any third party web sites or\n"
            + "services. You further acknowledge and agree that Safe Driving Simulator shall\n"
            + "not be responsible or liable, directly or indirectly, for any damage or loss\n"
            + "caused or alleged to be caused by or in connection with use of or reliance on\n"
            + "any such content, goods or services available on or through any such web sites\n" + "or services.\n"
            + "\n" + "We strongly advise you to read the terms and conditions and privacy policies\n"
            + "of any third-party web sites or services that you visit.\n" + "\n" + "Termination  \n" + "-----------\n"
            + "\n" + "We may terminate or suspend access to our Service immediately, without prior\n"
            + "notice or liability, for any reason whatsoever, including without limitation\n"
            + "if you breach the Terms.\n" + "\n"
            + "All provisions of the Terms which by their nature should survive termination\n"
            + "shall survive termination, including, without limitation, ownership\n"
            + "provisions, warranty disclaimers, indemnity and limitations of liability.\n" + "\n" + "Governing Law  \n"
            + "-------------\n" + "\n" + "These Terms shall be governed and construed in accordance with the laws of\n"
            + "Victoria, Australia, without regard to its conflict of law provisions.\n" + "\n"
            + "Our failure to enforce any right or provision of these Terms will not be\n"
            + "considered a waiver of those rights. If any provision of these Terms is held\n"
            + "to be invalid or unenforceable by a court, the remaining provisions of these\n"
            + "Terms will remain in effect. These Terms constitute the entire agreement\n"
            + "between us regarding our Service, and supersede and replace any prior\n"
            + "agreements we might have between us regarding the Service.\n" + "\n" + "Changes  \n" + "-------\n" + "\n"
            + "We reserve the right, at our sole discretion, to modify or replace these Terms\n"
            + "at any time. If a revision is material we will try to provide at least 30 days\n"
            + "notice prior to any new terms taking effect. What constitutes a material\n"
            + "change will be determined at our sole discretion.\n" + "\n"
            + "By continuing to access or use our Service after those revisions become\n"
            + "effective, you agree to be bound by the revised terms. If you do not agree to\n"
            + "the new terms, please stop using the Service.\n" + "\n" + "Contact Us  \n" + "----------\n" + "\n"
            + "If you have any questions about these Terms, please contact us.\n" + "\n" + "\n";

    /**
     * Called when controller starts
     */
    public void initialize() {
        termsCondTextArea.setText(terms);
    }

    /**
     * Called when the user accpets to the termsn and conditions
     *
     * @param event The mouse click event on the button
     * @throws IOException Thrown if fxml file not found
     */
    @FXML
    private void handleAccept(ActionEvent event) throws IOException {

        // Change scene to display simulation
        Parent root = FXMLLoader.load(getClass().getResource("../view/ChooseMode.fxml"));
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
