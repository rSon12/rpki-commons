/**
 * The BSD License
 *
 * Copyright (c) 2010, 2011 RIPE NCC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the RIPE NCC nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.ripe.commons.provisioning.interop;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.ripe.commons.certification.validation.ValidationCheck;
import net.ripe.commons.certification.validation.ValidationResult;
import net.ripe.commons.provisioning.cms.ProvisioningCmsObject;
import net.ripe.commons.provisioning.cms.ProvisioningCmsObjectParser;
import net.ripe.commons.provisioning.payload.AbstractProvisioningPayload;
import net.ripe.commons.provisioning.payload.issue.request.CertificateIssuanceRequestPayload;
import net.ripe.commons.provisioning.x509.pkcs10.RpkiCaCertificateRequestParser;
import net.ripe.commons.provisioning.x509.pkcs10.RpkiCaCertificateRequestParserException;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.junit.Ignore;
import org.junit.Test;

public class ProcessIscUpdownPdusTest {
    
    private static final String PATH_TO_TEST_PDUS = "src/test/resources/isc-interop-updown";
    
    @Test
    public void shouldParsePdu170() throws IOException, RpkiCaCertificateRequestParserException {
        byte[] encoded = FileUtils.readFileToByteArray(new File(PATH_TO_TEST_PDUS + "/pdu.200.der"));
        ProvisioningCmsObjectParser parser = new ProvisioningCmsObjectParser();
        parser.parseCms("cms", encoded);
        ValidationResult validationResult = parser.getValidationResult();
        for (ValidationCheck check:  validationResult.getFailures("cms")) {
            System.err.println("Failure: " + check);
        }
        ProvisioningCmsObject provisioningCmsObject = parser.getProvisioningCmsObject();
        
        CertificateIssuanceRequestPayload payload = (CertificateIssuanceRequestPayload) provisioningCmsObject.getPayload();
        
        PKCS10CertificationRequest certificateRequest = payload.getRequestElement().getCertificateRequest();
        
        RpkiCaCertificateRequestParser rpkiCaCertificateRequestParser = new RpkiCaCertificateRequestParser(certificateRequest);
        assertNotNull(rpkiCaCertificateRequestParser.getCaRepositoryUri());
        assertNotNull(rpkiCaCertificateRequestParser.getManifestUri());
        assertNotNull(rpkiCaCertificateRequestParser.getPublicKey());
    }
    
    @Test
    @Ignore
    public void shouldParseEmAll() throws IOException {
        String[] Files = new String[] { "pdu.170.der", "pdu.171.der", "pdu.172.der", "pdu.173.der", "pdu.180.der", "pdu.183.der", "pdu.184.der", "pdu.189.der", "pdu.196.der", "pdu.199.der", "pdu.200.der", "pdu.205.der" };
        for (String fileName: Files) {
            byte[] encoded = FileUtils.readFileToByteArray(new File(PATH_TO_TEST_PDUS + "/" + fileName));
            ProvisioningCmsObjectParser parser = new ProvisioningCmsObjectParser();
            parser.parseCms("cms", encoded);
            if (parser.getValidationResult().hasFailures()) {
                System.err.println("Failed to understand: " + fileName);
            } else {
                ProvisioningCmsObject provisioningCmsObject = parser.getProvisioningCmsObject();
                AbstractProvisioningPayload payload = provisioningCmsObject.getPayload();
                System.err.println("Processed file: " + fileName);
                System.err.println("Payload: " + payload.toString());
            }
        }
    }

}