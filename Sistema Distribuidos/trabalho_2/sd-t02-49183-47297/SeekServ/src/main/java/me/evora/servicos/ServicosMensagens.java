package me.evora.servicos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

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
    private ServicosLocalizacao servicosLocalizacao;
    private ServicosTipoDeArte servicosTipoDeArte;
    private ServicosAtuacao servicosAtuacao;
    private ServicosDonativo servicosDonativo;
    private ServicosDeUtilizador servicosDeUtilizador;

    public ServicosMensagens(InputStream entrada, OutputStream saida, 
            ServicosLocalizacao servicosLocalizacao, ServicosTipoDeArte servicosTipoDeArte,
            ServicosAtuacao servicosAtuacao, ServicosArtista servicosArtista, ServicosDonativo servicosDonativo,
            ServicosDeUtilizador servocosUtilizador) {
        this.entrada = entrada;
        this.saida = saida;
        this.servicosLocalizacao = servicosLocalizacao;
        this.servicosTipoDeArte = servicosTipoDeArte;
        this.servicosAtuacao = servicosAtuacao;
        this.servicosDonativo = servicosDonativo;
        this.servicosDeUtilizador = servocosUtilizador;
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

    public Object[] receberDadosDeNovoUtilizador() throws IOException, ErroDeEntidadeNaoEncontrada {
        String username = receberMensagemDeTexto();
        String email = receberMensagemDeTexto();
        String password = receberMensagemDeTexto();
        String tipoDeUtilizador = receberMensagemDeTexto();
        return new Object[] { username, email, password, TipoDeUtilizador.valueOf(tipoDeUtilizador) };
    }

    public Object[] receberDadosDeAutenticacao() throws IOException {
        String usernameOuEmail = receberMensagemDeTexto();
        String password = receberMensagemDeTexto();
        return new Object[] { usernameOuEmail, password };
    }

    public void enviarUtilizador(Utilizador utilizador) throws IOException {
        enviarMensagemInteiro(utilizador.getId());
        enviarMensagemDeTexto(utilizador.getUsername());
        enviarMensagemDeTexto(utilizador.getEmail());
        enviarMensagemDeTexto(utilizador.getTipo().name());
        enviarMensagemDeTexto(utilizador.getDataDeCriacao().getTime() + "");
    }

    public void enviarListarDeUtilizadores(List<Utilizador> utilizadores) throws IOException {
        enviarMensagemInteiro(utilizadores.size());
        for(Utilizador utilizador : utilizadores) {
            enviarUtilizador(utilizador);
        }
    }

    public Utilizador receberUtilizador() throws IOException, ErroDeEntidadeNaoEncontrada {
        int id = receberMensagemInteiro();
        return servicosDeUtilizador.pesquisarUtilizadorPorId(id);
    }

    public Object[] receberDadosDeNovoArtista() throws IOException, ErroDeEntidadeNaoEncontrada {
        String nome = receberMensagemDeTexto();
        String descricao = receberMensagemDeTexto();
        String foto = receberMensagemDeTexto();

        Atuacao atuacao = null;
        int atuacaoId = receberMensagemInteiro();
        if(atuacaoId <= 0) {
            Localizacao localizacao = receberLocalizacao();
            Date data = receberData();
            atuacao = new Atuacao(localizacao, data);
        } else {
            atuacao = servicosAtuacao.pesquisarAtuacaoPorId(atuacaoId);
        }

        TipoDeArte tipoDeArte = null;
        int tipoDeArteId = receberMensagemInteiro();
        if( tipoDeArteId <= 0 ) {
            String descricaoArte = receberMensagemDeTexto();
            tipoDeArte = new TipoDeArte(descricaoArte);
        } else {
            tipoDeArte = servicosTipoDeArte.pesquisarLocalizacaoPorId(tipoDeArteId);
        }
        return new Object[] { nome, descricao, foto, atuacao.getLocalizacao(), tipoDeArte };
    }

    public void enviarArtista(Artista artista) throws IOException {
        enviarMensagemInteiro(artista.getId());
        enviarMensagemDeTexto(artista.getNome());
        enviarMensagemDeTexto(artista.getDescricao());
        enviarMensagemDeTexto(artista.getFoto());
        enviarMensagemInteiro(artista.getEstaApurado() ? 1 : 0);
        enviarTipoDeArte(artista.getTipoDeArte());
        // enviarAtuacao(artista.getAtuacaoAtual());
        enviarMensagemDeTexto(artista.getMediaDasClassificacoes()+"");
        enviarMensagemDeTexto(artista.getDataDeCriacao().getTime()+"");
    }

    public void enviarListaDeArtistas(List<Artista> artistas) throws IOException {
        enviarMensagemInteiro(artistas.size());
        for(Artista artista : artistas) {
            enviarArtista(artista);
        }
    }

    public int receberArtistaID() throws IOException {
        int id = receberMensagemInteiro();
        return id;
    }

    public void enviarAtuacao(Atuacao atuacao) throws IOException {
        enviarMensagemInteiro(atuacao.getId());
        enviarLocalizacao(atuacao.getLocalizacao());
        enviarMensagemInteiro(atuacao.getEAtual() ? 1 : 0);
        enviarData(atuacao.getDataDeAtuacao());
    }

    public void enviarListaDeAtuacoes(List<Atuacao> atuacaos) throws IOException {
        enviarMensagemInteiro(atuacaos.size());
        for(Atuacao atuacao : atuacaos) {
            enviarAtuacao(atuacao);
        }
    }

    public Atuacao receberAtuacao () throws IOException, ErroDeEntidadeNaoEncontrada {
        int id = receberMensagemInteiro();
        return servicosAtuacao.pesquisarAtuacaoPorId(id);
    }

    public Atuacao receberDadosDeAtuacao () throws IOException {
        receberMensagemInteiro();
        Localizacao localizacao = receberLocalizacao();
        Date data = receberData();
        Atuacao atuacao = new Atuacao(localizacao, data);
        return atuacao;
    }

    public void enviarLocalizacao(Localizacao localizacao) throws IOException {
        enviarMensagemInteiro(localizacao.getId());
        enviarMensagemInteiro(localizacao.getLatitude());
        enviarMensagemInteiro(localizacao.getLongitude());
        enviarData(localizacao.getDataDeCricao());
    }

    public void enviarListaDeLocalizacoes(List<Localizacao> localizacoes) throws IOException {
        enviarMensagemInteiro(localizacoes.size());
        for(Localizacao localizacao : localizacoes) {
            enviarLocalizacao(localizacao);
        }
    }

    public Localizacao receberLocalizacao() throws IOException {
        int id = receberMensagemInteiro();
        if(id <= 0) {
            int latitude = receberMensagemInteiro();
            int longitude = receberMensagemInteiro();
            return new Localizacao(latitude, longitude);
        }
        Localizacao localizacao = servicosLocalizacao.pesquisarLocalizacaoPorId(id);
        return localizacao;
    }

    public void enviarTipoDeArte(TipoDeArte tipoDeArte) throws IOException {
        enviarMensagemInteiro(tipoDeArte.getId());
        enviarMensagemDeTexto(tipoDeArte.getDescricao());
        enviarData(tipoDeArte.getDataDeCriacao());
    }

    public void enviarListaDeTipoDeArtes(List<TipoDeArte> tipoDeArtes) throws IOException {
        enviarMensagemInteiro(tipoDeArtes.size());
        for(TipoDeArte tipoDeArte : tipoDeArtes) {
            enviarTipoDeArte(tipoDeArte);
        }
    }

    public TipoDeArte receberTipoDeArte() throws IOException {
        int id = receberMensagemInteiro();
        return servicosTipoDeArte.pesquisarLocalizacaoPorId(id);
    }

    public void enviarDonativo(Donativo donativo) throws IOException {
        enviarMensagemInteiro(donativo.getId());
        enviarArtista(donativo.getArtista());
        enviarUtilizador(donativo.getOferecedor());
        enviarMensagemDeTexto(donativo.getValor() + "");
        enviarData(donativo.getDataDeDoacao());
    }

    public void enviarListaDeDonativos(List<Donativo> donativos) throws IOException {
        enviarMensagemInteiro(donativos.size());
        for(Donativo donativo : donativos) {
            enviarDonativo(donativo);
        }
    }

    public Object[] receberDadosDonativo() throws IOException, ErroDeEntidadeNaoEncontrada {
        int artistaID = receberMensagemInteiro();
        String valor = receberMensagemDeTexto();
        int utilizadorID = receberMensagemInteiro();
        Utilizador utilizador = servicosDeUtilizador.pesquisarUtilizadorPorId(utilizadorID);
        return new Object[] { artistaID, Double.parseDouble(valor), utilizador };
    }

    public Donativo receberDonativo() throws ErroDeEntidadeNaoEncontrada, IOException {
        int id = receberMensagemInteiro();
        return servicosDonativo.pesquisarDonativoPorId(id);
    }

    public Object[] receberDadosClassificacao() throws IOException, ErroDeEntidadeNaoEncontrada {
        int artistaID = receberMensagemInteiro();
        String valor = receberMensagemDeTexto();
        String comentario = receberMensagemDeTexto();
        int utilizadorID = receberMensagemInteiro();
        Utilizador utilizador = servicosDeUtilizador.pesquisarUtilizadorPorId(utilizadorID);
        return new Object[] { artistaID, utilizador, Double.parseDouble(valor), comentario };
    }

    public FiltrosDeArtistas receberFiltros() throws IOException {
        FiltrosDeArtistas filtrosDeArtistas = new FiltrosDeArtistas();
        int idLocalizacao = receberMensagemInteiro();
        if(idLocalizacao > 0) {
            System.out.println("Tem localização");
            filtrosDeArtistas.localizacao = servicosLocalizacao.pesquisarLocalizacaoPorId(idLocalizacao);
        }
        int idTipoDeArte = receberMensagemInteiro();
        if(idTipoDeArte > 0) {
            System.out.println("Tem arte");
            filtrosDeArtistas.tipoDeArte = servicosTipoDeArte.pesquisarLocalizacaoPorId(idTipoDeArte);
        }
        return filtrosDeArtistas;
    }

    public void enviarData(Date data) throws IOException {
        enviarMensagemDeTexto(data.getTime() + "");
    }

    public Date receberData() throws IOException {
        String str = receberMensagemDeTexto();
        return new Date( Long.parseLong(str) );
    }
}
