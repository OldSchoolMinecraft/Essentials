package com.earth2me.essentials;

import java.math.*;
import java.util.logging.*;
import java.security.*;
import java.util.*;

class EssentialsErrorHandler extends Handler
{
    private final Map<BigInteger, String> errors;
    private final List<LogRecord> records;
    
    public EssentialsErrorHandler() {
        this.errors = new HashMap<BigInteger, String>();
        this.records = new LinkedList<LogRecord>();
    }
    
    @Override
    public void publish(final LogRecord lr) {
        if (lr.getThrown() == null || lr.getLevel().intValue() < Level.WARNING.intValue()) {
            return;
        }
        synchronized (this.records) {
            this.records.add(lr);
        }
    }
    
    @Override
    public void flush() {
        synchronized (this.records) {
            this.sortRecords();
        }
    }
    
    @Override
    public void close() throws SecurityException {
        synchronized (this.records) {
            this.sortRecords();
        }
    }
    
    private void sortRecords() {
        for (final LogRecord lr : this.records) {
            try {
                if (lr.getThrown() == null) {
                    return;
                }
                final Throwable tr = lr.getThrown();
                final StackTraceElement[] elements = tr.getStackTrace();
                if (elements == null || elements.length <= 0) {
                    return;
                }
                boolean essentialsFound = false;
                for (final StackTraceElement stackTraceElement : elements) {
                    if (stackTraceElement.getClassName().contains("com.earth2me.essentials")) {
                        essentialsFound = true;
                        break;
                    }
                }
                if (!essentialsFound && tr.getCause() != null) {
                    final Throwable cause = tr.getCause();
                    final StackTraceElement[] elements2 = cause.getStackTrace();
                    if (elements2 != null) {
                        for (final StackTraceElement stackTraceElement2 : elements2) {
                            if (stackTraceElement2.getClassName().contains("com.earth2me.essentials")) {
                                essentialsFound = true;
                                break;
                            }
                        }
                    }
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("[").append(lr.getLevel().getName()).append("] ").append(lr.getMessage()).append("\n");
                sb.append(tr.getMessage()).append("\n");
                for (final StackTraceElement stackTraceElement3 : tr.getStackTrace()) {
                    sb.append(stackTraceElement3.toString()).append("\n");
                }
                if (tr.getCause() != null && tr.getCause().getStackTrace() != null) {
                    sb.append(tr.getCause().getMessage()).append("\n");
                    for (final StackTraceElement stackTraceElement3 : tr.getCause().getStackTrace()) {
                        sb.append(stackTraceElement3.toString()).append("\n");
                    }
                }
                final String errorReport = sb.toString();
                final byte[] bytesOfMessage = errorReport.getBytes("UTF-8");
                final MessageDigest md = MessageDigest.getInstance("MD5");
                final BigInteger bi = new BigInteger(md.digest(bytesOfMessage));
                this.errors.put(bi, errorReport);
            }
            catch (Throwable t) {}
        }
        this.records.clear();
    }
    
    Map<BigInteger, String> getErrors() {
        return this.errors;
    }
}
