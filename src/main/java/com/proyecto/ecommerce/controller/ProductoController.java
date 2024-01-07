package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.service.IProductoService;
import com.proyecto.ecommerce.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private IProductoService IProductoService;

    @Autowired
    private UploadFileService upload;

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("productos", IProductoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
        LOGGER.info("Este es el objeto producto {}", producto);
        Usuario u = new Usuario(2, "", "", "", "", "", "", "");
        producto.setUsuario(u);

        //TODO: Imagen
        if (producto.getId() == null) { // Cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }

        IProductoService.save(producto);
        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Producto producto = new Producto();
        Optional<Producto> optionalProducto = IProductoService.get(id);
        producto = optionalProducto.get();

        LOGGER.info("Producto buscado: {}", producto);
        model.addAttribute("producto", producto);

        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {

        Producto p = new Producto();
        p = IProductoService.get(producto.getId()).get();

        if (file.isEmpty()) { // Editamos el producto pero no cambiamos la imagen
            producto.setImagen(p.getImagen());
        } else { // Cuando se edita la imagen
            // Eliminar cuando no sea la imagen por defecto
            if (!p.getImagen().equals("default.jpg")) upload.deleteImage(p.getImagen());
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        }
        producto.setUsuario(p.getUsuario());

        IProductoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        Producto p = new Producto();
        p = IProductoService.get(id).get();

        // Eliminar cuando no sea la imagen por defecto
        if (!p.getImagen().equals("default.jpg")) upload.deleteImage(p.getImagen());

        IProductoService.delete(id);
        return "redirect:/productos";
    }
}
