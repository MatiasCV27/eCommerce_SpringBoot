package com.proyecto.ecommerce.controller;

import com.proyecto.ecommerce.model.DetalleOrden;
import com.proyecto.ecommerce.model.Orden;
import com.proyecto.ecommerce.model.Producto;
import com.proyecto.ecommerce.model.Usuario;
import com.proyecto.ecommerce.service.IDetalleOrdenService;
import com.proyecto.ecommerce.service.IOrdenService;
import com.proyecto.ecommerce.service.IProductoService;
import com.proyecto.ecommerce.service.IUsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private IProductoService IProductoService;

    @Autowired
    private IUsuarioService IUsuarioService;

    @Autowired
    private IOrdenService iOrdenService;

    @Autowired
    private IDetalleOrdenService iDetalleOrdenService;

    // Para almacenar los detalles de la orden
    List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

    // Datos de la orden
    Orden orden = new Orden();

    @GetMapping("")
    public String Home(Model model, HttpSession session) {
        log.info("Sesion del usuario: {}", session.getAttribute("idusuario"));
        model.addAttribute("productos", IProductoService.findAll());

        // Session
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        return "usuario/home";
    }

    @GetMapping("productohome/{id}")
    public String productoHome(@PathVariable Integer id, Model model) {
        log.info("ID del producto enviado como parametro {}", id);

        Producto producto = new Producto();
        Optional<Producto> productoOptional = IProductoService.get(id);
        producto = productoOptional.get();

        model.addAttribute("producto", producto);

        return "usuario/productohome";
    }

    @PostMapping("/cart")
    public String addCar(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
        DetalleOrden detalleOrden = new DetalleOrden();
        Producto producto = new Producto();
        double sumaTotal = 0;

        Optional<Producto> optionalProducto = IProductoService.get(id);
        log.info("Producto añadido: {}", optionalProducto.get());
        log.info("Cantidad: {}", cantidad);
        producto = optionalProducto.get();

        detalleOrden.setCantidad(cantidad);
        detalleOrden.setPrecio(producto.getPrecio());
        detalleOrden.setNombre(producto.getNombre());
        detalleOrden.setTotal(producto.getPrecio() * cantidad);
        detalleOrden.setProducto(producto);

        // Validar que el producto no se añada 2 veces
        Integer idProducto = producto.getId();
        boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
        if (!ingresado) detalles.add(detalleOrden);

        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }

    //Quitar un producto del carrito
    @GetMapping("/delete/cart/{id}")
    public String deleteProductoCart(@PathVariable Integer id, Model model) {

        List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();
        for (DetalleOrden detalleOrden : detalles) {
            if(detalleOrden.getProducto().getId() != id) ordenesNuevas.add(detalleOrden);
        }

        //Poner nueva lista con los productos restantes
        detalles = ordenesNuevas;

        double sumaTotal = 0;
        sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

        orden.setTotal(sumaTotal);
        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        return "usuario/carrito";
    }

    @GetMapping("getCart")
    public String getCart(Model model, HttpSession session) {

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);

        // Sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        return "/usuario/carrito";
    }

    @GetMapping("/order")
    public String order(Model model, HttpSession session) {

        Usuario usuario = IUsuarioService.findbyId(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        model.addAttribute("cart", detalles);
        model.addAttribute("orden", orden);
        model.addAttribute("usuario", usuario);

        return "usuario/resumenorden";
    }

    // Guardar la orden
    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession session) {
        Date fechaCreacion = new Date();
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(iOrdenService.generarNumeroOrden());

        // Usuario
        Usuario usuario = IUsuarioService.findbyId(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        orden.setUsuario(usuario);
        iOrdenService.save(orden);

        // Guardar detalles
        for(DetalleOrden dt: detalles) {
            dt.setOrden(orden);
            iDetalleOrdenService.save(dt);
        }

        // Limpiar lista y orden
        orden = new Orden();
        detalles.clear();

        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam String nombre, Model model) {
        log.info("Nombre del producto: {}", nombre);
        List<Producto> productos = IProductoService.findAll().stream().filter(p -> p.getNombre()
                .contains(nombre)).collect(Collectors.toList());
        model.addAttribute("productos", productos);
        return "usuario/home";
    }

}
