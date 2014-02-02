package com.example;

/* 34567890123456789012345678901234567890123456789012345678901234567890123456789
 * *****************************************************************************
 * Copyright (c) 2010, M-PLIFY S.A.
 *                     68, avenue de la Liberté
 *                     L-1930 Luxembourg
 *
 * Released by M-PLIFY S.A. under the MIT License
 *******************************************************************************
 *******************************************************************************
 * Runtime Exception (so no need to declare it) thrown by "check" methods.
 * 
 * 2014.02.01 - Namespace changed from "com.mplify.checkers" to "com.example"
 *              for some neutrality.     
 ******************************************************************************/

@SuppressWarnings("serial")
public class CheckFailedException extends RuntimeException {

    public CheckFailedException() {
        super();
    }

    public CheckFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckFailedException(String message) {
        super(message);
    }

    public CheckFailedException(Throwable cause) {
        super(cause);
    }
    
}