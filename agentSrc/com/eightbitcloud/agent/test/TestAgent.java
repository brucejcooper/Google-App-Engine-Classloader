package com.eightbitcloud.agent.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.eightbitcloud.appengine.classloader.Agent;

public class TestAgent implements Agent {
    @Override
    public String execute(String[] params) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(TestAgent.class.getResourceAsStream("test.txt")));
        String val = r.readLine();
        r.close();
        return val;
    }
}
