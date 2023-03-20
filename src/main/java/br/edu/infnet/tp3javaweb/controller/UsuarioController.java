package br.edu.infnet.tp3javaweb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import br.edu.infnet.tp3javaweb.model.Endereco;
import br.edu.infnet.tp3javaweb.model.Imagem;
import br.edu.infnet.tp3javaweb.model.Usuario;
import br.edu.infnet.tp3javaweb.service.StorageService;
import br.edu.infnet.tp3javaweb.service.UsuarioService;

@Controller
public class UsuarioController {
    private String msg;
    private String alert;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/buscar")
    public String buscar(Model model, @RequestParam String cep) {

            try {
                URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");
                BufferedReader resposta = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                String json = resposta.lines().collect(Collectors.joining());
                Gson gson = new Gson();
                Endereco endereco = gson.fromJson(json, Endereco.class);

                model.addAttribute("endereco", endereco);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return "index";
            }

        return "cadastro";
    }

    @GetMapping(value = "/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping(value = "/cadastro")
    public String cadastrar(Usuario usuario, Endereco endereco, @RequestParam("image") MultipartFile image) {
        usuario.setEndereco(endereco);

        String imgUrl = storageService.uploadImage(image);
        String[] fields= imgUrl.split("/");
        String imgName = fields[fields.length-1];
        Imagem newImage = new Imagem(imgName, imgUrl);

        usuario.setImagem(newImage);
        usuarioService.save(usuario);

        msg = "A inclusão do usuário '" + usuario.getNome() + "' foi realizada com sucesso!";
        alert = "alert alert-success";

        return "redirect:/lista";
    }

    @GetMapping(value = "/lista")
    public String lista(Model model) {
        model.addAttribute("mensagem", msg);
        model.addAttribute("alerta", alert);
        model.addAttribute("usuarios", usuarioService.findAll());
        return "lista";
    }

    @GetMapping(value = "deletar/{id}")
    public String deletar(@PathVariable Integer id) {
        Usuario usuario = usuarioService.findById(id);
        usuarioService.delete(id);
        storageService.deleteImg(usuario.getImagem().getName());

        msg = "O usuário '" + usuario.getNome() + "' foi excluído!";
        alert = "alert alert-danger";
        return "redirect:/lista";
    }
 }
