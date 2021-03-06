package controllers;

import db.DBHelper;
import db.DBVisit;
import db.DBVisitor;
import models.paddocks.Paddock;
import models.visitors.Visit;
import models.visitors.Visitor;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;


public class VisitorsController {

    public VisitorsController() {
        setupEndpoints();
    }

    public static void setupEndpoints(){

        get("/visitors/visits", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int visitorId = Integer.parseInt(req.queryParams("visitor_id"));
            Visitor visitor = DBHelper.find(visitorId, Visitor.class);
            model.put("template", "templates/visitors/visits/index.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        post("/visitors/visits", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int visitorId = Integer.parseInt(req.queryParams("visitor_id"));
            Visitor visitor = DBHelper.find(visitorId, Visitor.class);
            Visit visit = new Visit(visitor);
            DBHelper.save(visit);
            res.redirect("/visitors/" + visitor.getId() + "/visit");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/visitors", (req, res) -> {
            List<Visitor> visitors = DBHelper.getAll(Visitor.class);
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/visitors/index.vtl");
            model.put("visitors", visitors);
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/visitors/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/visitors/new.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/visitors/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/visitors/login.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/visitors/user_taken", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/visitors/user_taken.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        //show
        get("visitors/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int visitorId = Integer.parseInt(req.params(":id"));
            Visitor visitor = DBHelper.find(visitorId, Visitor.class);
            model.put("visitor", visitor);
            model.put("template", "templates/visitors/show.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        //edit


        get("/visitors/:id/edit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int visitorId = Integer.parseInt(req.params(":id"));
            Visitor visitor = DBHelper.find(visitorId, Visitor.class);
            model.put("visitor", visitor);
            model.put("template", "templates/visitors/edit.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());


        post("/visitors/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String username = req.queryParams("username");

            if(DBVisitor.checkIfVisitorByUsernameExists(username)){
                Visitor foundVisitor = DBVisitor.findVisitorByUsername(username);
                model.put("foundVisitor", foundVisitor);
                res.redirect("/visitors/" + foundVisitor.getId());
            }else{
                model.put("template", "templates/visitors/invalid_username.vtl");
            }
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        get("/visitors/:id/visit", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            int visitorId = Integer.parseInt(req.params(":id"));
            Visitor visitor = DBHelper.find(visitorId, Visitor.class);
            Visit visit = DBVisit.getMostRecentVisit(visitor);
            List<Paddock> paddocks =  DBVisit.getAllPaddocksForVisit(visit);
            model.put("paddocks", paddocks);
            model.put("visitor", visitor);
            model.put("template", "templates/visitors/visits/show.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());

        //update
        post("/visitors/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String email = req.queryParams("email");
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            String username = req.queryParams("username");
            int visitorId = Integer.parseInt(req.params(":id"));
            Visitor visitor = DBHelper.find(visitorId, Visitor.class);
            visitor.setFirstName(firstName);
            visitor.setEmail(email);
            visitor.setLastName(lastName);
            visitor.setUsername(username);
            DBHelper.update(visitor);
            res.redirect("/visitors/" + visitor.getId());
            return new ModelAndView(model, "templates/layout.vtl");
        }, new VelocityTemplateEngine());
        //create
        post("/visitors", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String firstName = req.queryParams("firstName");
            String lastName = req.queryParams("lastName");
            String username = req.queryParams("username");
            String email = req.queryParams("email");
            if(!DBVisitor.checkIfVisitorByUsernameExists(username)) {
                Visitor visitor = new Visitor(firstName, lastName, username, email);
                visitor.setEmail(email);
                DBHelper.save(visitor);
                res.redirect("/visitors/login");
            } else {
                res.redirect("/visitors/user_taken");
            }
            return new ModelAndView(model, "templates/layout.vtl");
        });







    }
}
