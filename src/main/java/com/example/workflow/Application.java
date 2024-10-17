package com.example.workflow;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.camunda.bpm.spring.boot.starter.SpringBootProcessApplication;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class Application extends SpringBootProcessApplication {

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  @PostDeploy
  public void evaluateDecisionTable(ProcessEngine engine) {
    DecisionService decisionService = engine.getDecisionService();
    VariableMap variables = Variables.createVariables().putValue("podanie_punktyECTS", 16)
            .putValue("podanie_uzasadnienie", "asdfghjkloiuytrewq");
    DmnDecisionTableResult decisionTableResult = decisionService.evaluateDecisionTableByKey("OcenaPodania", variables);
    DmnDecisionRuleResult decisionRuleResult = decisionTableResult.getSingleResult();
    if(decisionTableResult.getSingleResult().containsKey("decyzja_czyZaliczono")){
      Boolean zal = decisionTableResult.getSingleResult().getEntry("decyzja_czyZaliczono");
      System.out.println("Czy zaliczono " + zal);
    }

    StringValue uzasadnienie = decisionRuleResult.getEntryTyped("decyzja_uzasadnienie");
    System.out.println("Uzasadnienie " + uzasadnienie.getValue());

    for(Map<String, Object> res : decisionTableResult.getResultList()) {
      for(Map.Entry<String, Object> entry : res.entrySet()) {
        System.out.println("Key = " + entry.getKey() + " Value = " + entry.getValue());
      }
    }
  }

}