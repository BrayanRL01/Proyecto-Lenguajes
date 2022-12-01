package com.aplicacion.negocio.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aplicacion.negocio.entity.Roles;
import com.aplicacion.negocio.service.RolesService;

@Controller
public class RolesController {

    @Autowired
    RolesService RS;

    @GetMapping("/listaroles")
    public String Index(Model M) throws SQLException {
        List<Roles> ListaRoles = RS.ObtenerRoles();
        M.addAttribute("Roles", ListaRoles);
        return "listaroles";
    }

    @GetMapping("/nuevorol")
    public String CrearRol(Model M) throws SQLException {
        M.addAttribute("titulo", "Crear Rol");
        M.addAttribute("Rol", new Roles());
        M.addAttribute("boton", "Crear");
        return "nuevorol";
    }

    @PostMapping("/GuardarRol")
    public String GuardarRol(@ModelAttribute Roles R, RedirectAttributes flash) throws SQLException {
        RS.InsertarRoles(R);
        flash.addFlashAttribute("mensaje", "Rol creado con éxito.");
        return "redirect:/listaroles";
    }

    @GetMapping("/ModificarRol/{Rol_Id}")
    public String ModificarRol(@PathVariable("Rol_Id") Long Rol_Id, Model M)
            throws SQLException {
        Roles R = RS.ObtenerRolPorID(Rol_Id);
        M.addAttribute("titulo", "Editar Rol");
        M.addAttribute("Rol", R);
        M.addAttribute("boton", "Actualizar");
        return "modificarrol";
    }

    @PostMapping("/EditarRol")
    public String EditarRol(@ModelAttribute Roles R, RedirectAttributes flash) throws SQLException {
        RS.ModificarRol(R);
        flash.addFlashAttribute("mensaje", "Rol actualizado con éxito.");
        return "redirect:/listaroles";
    }

    @GetMapping("/EliminarRol/{Rol_Id}")
    public String EliminarRol(@PathVariable("Rol_Id") Long Rol_Id, RedirectAttributes flash)
            throws SQLException {
        RS.EliminarRol(Rol_Id);
        flash.addFlashAttribute("mensaje", "Rol eliminado con éxito.");
        return "redirect:/listaroles";
    }
}