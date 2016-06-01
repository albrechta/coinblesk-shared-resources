/*
 * Copyright 2016 The Coinblesk team and the CSG Group at University of Zurich
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.coinblesk.json;

import java.util.List;

import com.coinblesk.util.Pair;

/**
 *
 * @author Andreas Albrecht
 * 
 */
public class SignVerifyTO extends BaseTO<SignVerifyTO> {

    //choice 1
    private byte[] transaction; // input
    
    //choice 2
    private String addressTo; // input
    private long amountToSpend; // input 
    private long amountChange; // input
   
    private List<TxSig> signatures; // input/output
    
    private Pair<byte[], TxSig> payeeSig; // input/output, not signed.
    
    public byte[] transaction() {
	    return transaction;
	}

	public SignVerifyTO transaction(byte[] transaction) {
        this.transaction = transaction;
        return this;
    }
    
    public List<TxSig> signatures() {
	    return signatures;
	}

	public SignVerifyTO signatures(List<TxSig> serverSignatures) {
        this.signatures = serverSignatures;
        return this;
    }
    
    public long amountToSpend() {
	    return amountToSpend;
	}

	public SignVerifyTO amountToSpend(long amountToSpend) {
        this.amountToSpend = amountToSpend;
        return this;
    }

    public String addressTo() {
	    return addressTo;
	}

	public SignVerifyTO addressTo(String addressTo) {
        this.addressTo = addressTo;
        return this;
    }

    public long amountChange() {
    	return amountChange;
    }
    
    public SignVerifyTO amountChange(long amountChange) {
    	this.amountChange = amountChange;
    	return this;
    }
    
    public Pair<byte[], TxSig> payeeSig() {
    	return payeeSig;
    }
    
    public SignVerifyTO payeeSig(Pair<byte[], TxSig> payeeSig) {
    	this.payeeSig = payeeSig;
    	return this;
    }
}