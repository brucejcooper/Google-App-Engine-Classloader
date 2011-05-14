package com.eightbitcloud.agent.test.simple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.eightbitcloud.appengine.testing.Agent;

public class TestAgentInOneJar implements Agent {
    @Override
    public String execute(String[] params) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(TestAgentInOneJar.class.getResourceAsStream("test.txt")));
        String val = r.readLine();
        r.close();
        return val;
    }
}
