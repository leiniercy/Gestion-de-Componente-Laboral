/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.application.views.vicedecano.lista_tareas;

import com.example.application.data.entity.Tarea;
import com.example.application.data.service.TareaService;
import com.example.application.views.MainLayout;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.io.FileNotFoundException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Leinier
 */
@PageTitle("Reporte")
@Route(value = "reporte-tareas-view", layout = MainLayout.class)
@RolesAllowed("vicedecano")
public class ReporteTareasView extends HorizontalLayout {

    private TareaService tareaService;

    public ReporteTareasView(
            @Autowired TareaService tareaService
    ) throws FileNotFoundException {

        addClassNames("reporte-tareas-view");
        this.tareaService = tareaService;

        String path = "E:\\miPDf.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        //Titulo
        float col = 280f;
        float columnWidth[] = {col, col};

        Table titleTable = new Table(columnWidth);

        titleTable.setBackgroundColor(new DeviceRgb(63, 169, 219)).setFontColor(Color.WHITE);

        titleTable.addCell(new Cell().add("Facultad 4")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMarginTop(30f)
                .setMarginBottom(30f)
                .setFontSize(30f)
                .setBorder(Border.NO_BORDER)
        );
        titleTable.addCell(new Cell().add("Reporte \n Componente Laboral \nUniveridad de Ciencias Informáticas")
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(30f)
                .setMarginBottom(30f)
                .setMarginRight(10f)
                .setBorder(Border.NO_BORDER)
        );

        //Informacion Personal
        float itemPersonalInfoColWidth[] = {80, 300, 100, 80};
        Table perosnalInfo = new Table(itemPersonalInfoColWidth);

        perosnalInfo.addCell(new Cell(0, 4)
                .add("Información")
                .setBold()
                .setBorder(Border.NO_BORDER)
        );

        perosnalInfo.addCell(new Cell().add("Nombre:").setBorder(Border.NO_BORDER));
        perosnalInfo.addCell(new Cell().add(".....").setBorder(Border.NO_BORDER));
        perosnalInfo.addCell(new Cell().add("Categoría:").setBorder(Border.NO_BORDER));
        perosnalInfo.addCell(new Cell().add(".....").setBorder(Border.NO_BORDER));

        perosnalInfo.addCell(new Cell().add("Departamento:").setBorder(Border.NO_BORDER));
        perosnalInfo.addCell(new Cell().add(".....").setBorder(Border.NO_BORDER));
        perosnalInfo.addCell(new Cell().add("Fecha:").setBorder(Border.NO_BORDER));
        perosnalInfo.addCell(new Cell().add(".....").setBorder(Border.NO_BORDER));

        //Tabla con el Reporte
        float itemInfoColWidth[] = {140, 140, 140, 140, 140};

        Table itemInfoTable = new Table(itemInfoColWidth);

        itemInfoTable.addCell(new Cell().add("Nombre")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
        );
        itemInfoTable.addCell(new Cell().add("Descripción")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
        );
        itemInfoTable.addCell(new Cell().add("Inicio")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setTextAlignment(TextAlignment.RIGHT)
        );
        itemInfoTable.addCell(new Cell().add("Fin")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setTextAlignment(TextAlignment.RIGHT)
        );
        itemInfoTable.addCell(new Cell().add("Estudiante")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setTextAlignment(TextAlignment.RIGHT)
        );

        List<Tarea> tareaList = tareaService.findAllTareas();

        for (int i = 0; i < tareaList.size(); i++) {
            String name = tareaList.get(i).getNombre();
            String description = tareaList.get(i).getDescripcion();
            String initDate = tareaList.get(i).getFecha_inicio().toString();
            String endDate = tareaList.get(i).getFecha_fin().toString();
            String student = tareaList.get(i).getE().getStringNombreApellidos();

            itemInfoTable.addCell(new Cell().add(name));
            itemInfoTable.addCell(new Cell().add(description));
            itemInfoTable.addCell(new Cell().add(initDate).setTextAlignment(TextAlignment.RIGHT));
            itemInfoTable.addCell(new Cell().add(endDate).setTextAlignment(TextAlignment.RIGHT));
            itemInfoTable.addCell(new Cell().add(student) .setTextAlignment(TextAlignment.RIGHT));

        }

        itemInfoTable.addCell(new Cell().add("")
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
        );
        itemInfoTable.addCell(new Cell().add("")
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
        );
        itemInfoTable.addCell(new Cell().add("")
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
        );
        itemInfoTable.addCell(new Cell().add("")
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
        );
        itemInfoTable.addCell(new Cell().add(String.format("Total: %d", tareaList.size()))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
        );

        document.add(titleTable);
        document.add(new Paragraph("\n"));
        document.add(perosnalInfo);
        document.add(new Paragraph("\n"));
        document.add(itemInfoTable);
        document.add(new Paragraph("\n (Firma: ...)")
                .setTextAlignment(TextAlignment.RIGHT)
        );
        document.close();

    }

}
