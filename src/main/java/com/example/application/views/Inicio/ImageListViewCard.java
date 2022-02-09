/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.Inicio;


import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
/**
 *
 * @author Leinier
 */
@JsModule("./views/imagelist/image-list-view-card.ts")
@Tag("image-list-view-card")
public class ImageListViewCard extends LitTemplate{
    @Id
    private Image image;

    @Id
    private Span header;

    @Id
    private Span subtitle;

    @Id
    private Span movil;

    @Id
    private Span universidad;

    @Id
    private Span facultad;    
    
    public ImageListViewCard(String text, String url, String title, String subtitle, String movil) {
        this.image.setSrc(url);
        this.image.setAlt(text);
        this.header.setText(title);
        this.subtitle.setText(subtitle);
        this.movil.setText(movil);
        this.universidad.setText("Universidad de Ciencias Informáticas");
        this.facultad.setText("Facultad 4");
    }
}
