package me.evora.servicos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.evora.contants.Constantes;
import me.evora.contants.TipoDeUtilizador;
import me.evora.modelo.Artista;
import me.evora.modelo.Atuacao;
import me.evora.modelo.Donativo;
import me.evora.modelo.Localizacao;
import me.evora.modelo.TipoDeArte;
import me.evora.modelo.Utilizador;
import me.evora.modelo.dto.FiltrosDeArtistas;
import me.evora.servicos.erros.ErroDeEntidadeNaoEncontrada;

public class ServicosMensagens {
    private InputStream entrada;
    private OutputStream saida;

    public ServicosMensagens(InputStream entrada, OutputStream saida) {
        this.entrada = entrada;
        this.saida = saida;
    }

    public void enviarMensagemDeTexto(String message) throws IOException {
        byte[] bytesDaMensagem = message.getBytes();
        int tamanhoDaMensagem = bytesDaMensagem.length;
        enviarMensagemInteiro(tamanhoDaMensagem);
        saida.write(bytesDaMensagem);
    }

    public String receberMensagemDeTexto() throws IOException {
        int tamanhoDaMensagem = receberMensagemInteiro();
        byte[] bytesDaMensagem = new byte[tamanhoDaMensagem];
        entrada.read(bytesDaMensagem);
        return new String(bytesDaMensagem);
    }

    public void enviarInteiroComoString(String message) throws IOException {
        byte[] bytesDaMensagem = message.getBytes();
        int tamanhoDaMensagem = bytesDaMensagem.length;
        saida.write(tamanhoDaMensagem);
        saida.write(bytesDaMensagem);
    }

    public String receberInteiroComoString() throws IOException {
        int tamanhoDaMensagem = entrada.read();
        byte[] bytesDaMensagem = new byte[tamanhoDaMensagem];
        entrada.read(bytesDaMensagem);
        return new String(bytesDaMensagem);
    }

    public void enviarMensagemInteiro(int inteiro) throws IOException {
        enviarInteiroComoString(inteiro + "");
    }

    public int receberMensagemInteiro() throws IOException {
        return Integer.parseInt(receberInteiroComoString());
    }

    public void enviarDadosDeUmNovoArtista(String nome, String descricao, String foto,
        Localizacao localizacao, TipoDeArte tipoDeArte) throws IOException {
        enviarMensagemInteiro(Constantes.PEDIR_REGITAR_UM_ARTISTA);
        enviarMensagemDeTexto(nome);
        enviarMensagemDeTexto(descricao);
        enviarMensagemDeTexto(foto);
        
        Atuacao atuacao = new Atuacao();
        atuacao.setDataDeAtuacao(new Date());
        atuacao.setLocalizacao(localizacao);
        enviarAtuacao(atuacao);
        
        enviarMensagemInteiro(tipoDeArte.getId());
        if( tipoDeArte.getId() <= 0 ) {
            enviarMensagemDeTexto(tipoDeArte.getDescricao());
        }
    }

    public void enviarDadosAtuacao(int artistaID, int latitude, int longitude, Date data) throws IOException {
        enviarMensagemInteiro(Constantes.DEFINIR_LOCAL_DE_ATUACAO_DE_UM_ARTISTA);
        enviarArtistaID(artistaID);
        Atuacao atuacao = new Atuacao();
        atuacao.setDataDeAtuacao(data);
        atuacao.setLocalizacao(new Localizacao(latitude, longitude));
        enviarAtuacao(atuacao);
    }

    public void enviarDadosDeUmUtilizador(String username, String email, String password,
        String tipo) throws IOException {
        enviarMensagemInteiro(Constantes.REGISTAR_UTILIZADOR);
        enviarMensagemDeTexto(username);
        enviarMensagemDeTexto(email);
        enviarMensagemDeTexto(password);
        enviarMensagemDeTexto(tipo);
    }

    public Utilizador reberUtilizador() throws IOException {
        int id = receberMensagemInteiro();
        String username = receberMensagemDeTexto();
        String email = receberMensagemDeTexto();
        String tipo = receberMensagemDeTexto();
        Date data = receberData();
        Utilizador u = new Utilizador(username, email, null);
        u.setTipo(TipoDeUtilizador.valueOf(tipo));
        u.setId(id);
        u.setDataDeCriacao(data);
        return u;
    }

    public void enviarDadosDeAutenticacao(String usernameOuEmail, String password) throws IOException {
        enviarMensagemInteiro(Constantes.AUTENTICAR_UTILIZADOR);
        enviarMensagemDeTexto(usernameOuEmail);
        enviarMensagemDeTexto(password);
    }

    public void enviarArtistaID(int id) throws IOException {
        enviarMensagemInteiro(id);;
    }

    public Artista receberArtista() throws IOException {
        int id = receberMensagemInteiro();
        String nome = receberMensagemDeTexto();
        String descricao = receberMensagemDeTexto();
        String foto = receberMensagemDeTexto();
        int estaApurado = receberMensagemInteiro(); 
        TipoDeArte tipoDeArte = receberTipoDeArte();
        double classificacao = Double.parseDouble( receberMensagemDeTexto() );
        Date dataDeCriacao = receberData();

        Artista artista = new Artista(nome, tipoDeArte, descricao, foto);
        artista.setId(id);
        artista.setDataDeCriacao(dataDeCriacao);
        artista.setEstaApurado(estaApurado == 1);
        artista.setClassificacao(classificacao);

        return artista;
    }

    public List<Artista> receberListaDeArtistas(FiltrosDeArtistas filtrosDeArtistas) throws IOException {
        enviarMensagemInteiro(Constantes.LISTAR_ARTISTAS_COM_OPCAO_FILTROS);
        enviarFiltros(filtrosDeArtistas);
        int quantidade = receberMensagemInteiro();
        List<Artista> artistas = new ArrayList<>();
        for(int i=0; i < quantidade; ++i) {
            artistas.add(receberArtista());
        }
        return artistas;
    }

    public List<Utilizador> receberListaDeUtilizadores() throws IOException {
        enviarMensagemInteiro(Constantes.LISTAR_UTILIZADORES_GERAIS);
        int quantidade = receberMensagemInteiro();
        List<Utilizador> utilizadores = new ArrayList<>();
        for(int i=0; i < quantidade; ++i) {
            utilizadores.add(reberUtilizador());
        }
        return utilizadores;
    }


    public void enviarNovoDadosDoArtista(int id, String nome, String descricao, String foto,
        Localizacao localizacao, TipoDeArte tipoDeArte) throws IOException {
        enviarMensagemInteiro(Constantes.EDITAR_ARTISTA);
        enviarArtistaID(id);
        enviarMensagemDeTexto(nome);
        enviarMensagemDeTexto(descricao);
        enviarMensagemDeTexto(foto);
        
        Atuacao atuacao = new Atuacao();
        atuacao.setDataDeAtuacao(new Date());
        atuacao.setLocalizacao(localizacao);
        enviarAtuacao(atuacao);
        
        enviarMensagemInteiro(tipoDeArte.getId());
        if( tipoDeArte.getId() <= 0 ) {
            enviarMensagemDeTexto(tipoDeArte.getDescricao());
        }
    }

    public List<Artista> receberListaDeArtistasPorEstado(boolean estado) throws IOException {
        enviarMensagemInteiro(Constantes.LISTAR_ARTISTAS_POR_ESTADO);
        enviarMensagemInteiro(estado ? 1 : 0);
        int quantidade = receberMensagemInteiro();
        List<Artista> artistas = new ArrayList<>();
        for(int i=0; i < quantidade; ++i) {
            artistas.add(receberArtista());
        }
        return artistas;
    }

    public void enviarAtuacao(Atuacao atuacao) throws IOException {
        enviarMensagemInteiro(atuacao.getId());
        if(atuacao.getId() <= 0) {
            enviarLocalizacao(atuacao.getLocalizacao());
            enviarData(atuacao.getDataDeAtuacao());
        }
    }

    public List<Atuacao> receberListaDeAtuacoes() throws IOException {
        int quantidade = receberMensagemInteiro();
        List<Atuacao> atuacaos = new ArrayList<>();
        for(int i=0; i < quantidade; ++i) {
            atuacaos.add(receberAtuacao());
        }
        return atuacaos;
    }

    public Atuacao receberAtuacao () throws IOException {
        int id = receberMensagemInteiro();
        if(id <= 0) {
            return null;
        }
        Localizacao localizacao = receberLocalizacao();
        int atual = receberMensagemInteiro();
        Date dataAtauacao = receberData();
        Atuacao atuacao = new Atuacao(localizacao, dataAtauacao);
        atuacao.seteAtual(atual == 1);
        atuacao.setId(id);
        return atuacao;
    }

    public void enviarLocalizacao(Localizacao localizacao) throws IOException {
        enviarMensagemInteiro(localizacao.getId());
        if(localizacao.getId() <= 0) {
            enviarMensagemInteiro(localizacao.getLatitude());
            enviarMensagemInteiro(localizacao.getLongitude());
        }
    }

    public List<Localizacao> receberListaDeLocalizacoes() throws IOException {
        enviarMensagemInteiro(Constantes.LISTA_DE_TODAS_LOCALIZACOES);
        int quantidade = receberMensagemInteiro();
        List<Localizacao> localizacoes = new ArrayList<>();
        for(int i=0; i < quantidade; ++i){
            localizacoes.add(receberLocalizacao());
        }
        return localizacoes;
    }

    public List<Localizacao> receberListaDeLocalizacoes(int operacao) throws IOException {
        enviarMensagemInteiro(operacao);
        int quantidade = receberMensagemInteiro();
        List<Localizacao> localizacoes = new ArrayList<>();
        for(int i=0; i < quantidade; ++i){
            localizacoes.add(receberLocalizacao());
        }
        return localizacoes;
    }

    public Localizacao receberLocalizacao() throws IOException {
        int id = receberMensagemInteiro();
        int latitude = receberMensagemInteiro();
        int longitude = receberMensagemInteiro();
        Date data = receberData();
        Localizacao localizacao = new Localizacao(latitude, longitude);
        localizacao.setId(id);
        localizacao.setDataDeCricao(data);
        return localizacao;
    }

    public void enviarTipoDeArte(TipoDeArte tipoDeArte) throws IOException {
        enviarMensagemInteiro(tipoDeArte.getId());
        if(tipoDeArte.getId() <= 0) {
            enviarMensagemDeTexto(tipoDeArte.getDescricao());
        }
    }

    public List<TipoDeArte> receberListaDeTiposDeArte() throws IOException {
        enviarMensagemInteiro(Constantes.LISTA_DE_TODOS_TIPOS_DE_ARTES);
        int quantidade = receberMensagemInteiro();
        List<TipoDeArte> tiposDeArte = new ArrayList<>();
        for(int i=0; i < quantidade; ++i) {
            tiposDeArte.add(receberTipoDeArte());
        }
        return tiposDeArte;
    }

    public TipoDeArte receberTipoDeArte() throws IOException {
        int id = receberMensagemInteiro();
        String descricao = receberMensagemDeTexto();
        Date data = receberData();
        TipoDeArte tipoDeArte = new TipoDeArte(descricao);
        tipoDeArte.setId(id);
        tipoDeArte.setDataDeCriacao(data);
        return tipoDeArte;
    }

    public void enviarDonativo(Donativo donativo) throws IOException {
        enviarArtistaID(donativo.getArtista().getId());
        enviarMensagemDeTexto(donativo.getValor() + "");
        enviarUilizador(donativo.getOferecedor());
    }

    public void enviarUilizador(Utilizador utilizador) throws IOException {
        enviarMensagemInteiro(utilizador.getId());
        if(utilizador.getId() <= 0) {
            enviarMensagemDeTexto(utilizador.getUsername());
            enviarMensagemDeTexto(utilizador.getEmail());
            enviarMensagemDeTexto(utilizador.getPassword());
            enviarMensagemDeTexto(utilizador.getTipo().toString());
        }
    }

    public List<Donativo> receberListaDeDonativos() throws IOException {
        int quantidade = receberMensagemInteiro();
        List<Donativo> donativos = new ArrayList<>();
        for(int i=0; i < quantidade; ++i) {
            donativos.add(receberDonativo());
        }
        return donativos;
    }

    public Donativo receberDonativo() throws IOException {
        int id = receberMensagemInteiro();
        Artista artista = receberArtista();
        Utilizador utilizador = reberUtilizador();
        double valor = Double.parseDouble( receberMensagemDeTexto() );
        Date data = receberData();
        Donativo donativo = new Donativo(valor, artista, utilizador);
        donativo.setId(id);
        donativo.setDataDeDoacao(data);
        return donativo;
    }

    public void enviarDadosClassificacao(int artistaID, double classe, String comentario, 
            int utilizadorID) throws IOException {
        enviarArtistaID(artistaID);
        enviarMensagemDeTexto(classe+"");
        enviarMensagemDeTexto(comentario);
        enviarMensagemInteiro(utilizadorID);
    }

    public void enviarFiltros(FiltrosDeArtistas filtros) throws IOException {
        if(filtros.localizacao == null) {
            enviarMensagemInteiro(0);
        } else {
            enviarLocalizacao(filtros.localizacao);
        }
      
        if(filtros.tipoDeArte == null) {
            enviarMensagemInteiro(0);
        } else {
            enviarTipoDeArte(filtros.tipoDeArte);
        }
    }

    public void enviarData(Date data) throws IOException {
        if(data == null) {
            enviarMensagemInteiro(0);
            return;
        }
        enviarMensagemDeTexto(data.getTime() + "");
    }

    public Date receberData() throws IOException {
        String dateStr = receberMensagemDeTexto();
        return new Date(Long.parseLong(dateStr));
    }
}
