package com.vgu.cs.common.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class VApiHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        this.doHandle(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) {
        this.doHandle(req, res);
    }

    protected abstract void doHandle(HttpServletRequest req, HttpServletResponse res);
}
