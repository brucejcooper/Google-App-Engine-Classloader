package com.eightbitcloud.agent.test.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.eightbitcloud.appengine.testing.Agent;
import com.eightbitcloud.agent.test.jar.ClassInAJar;

public class TestAgentInCompoundJar implements Agent {
    @Override
    public String execute(String[] params) throws IOException {
        return new ClassInAJar().doSomething();
    }
}
