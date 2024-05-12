package co.edu.unbosque.dao;

//PRUEBA DAO
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import co.edu.unbosque.dto.*;

public class CancionDAO {

	private static final String sitio = "http://localhost:8088/";

	public static int crearCancion(CancionDTO cancion) throws IOException {
		URL url = new URL(sitio + "Canciones/guardar");

		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		try {
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.setRequestProperty("Content-Type", "application/json");

			// Construcción del objeto JSON para la canción
			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put("nombreCancion", cancion.getNombreCancion());
			jsonRequest.put("nombreArtista", cancion.getNombreArtista());
			jsonRequest.put("generoMusical", cancion.getGeneroMusical());
			jsonRequest.put("rutaDelArchivo", cancion.getRutaDelArchivo());
			jsonRequest.put("nombreEmisora", cancion.getNombreEmisora());

			OutputStream outputStream = http.getOutputStream();
			outputStream.write(jsonRequest.toString().getBytes(StandardCharsets.UTF_8));
			outputStream.flush();

			int respuesta = http.getResponseCode();
			return respuesta;
		} finally {
			http.disconnect();
		}
	}

	public static int actualizarCancion(String nombreCancion, CancionDTO nuevaCancion) throws IOException {
		URL url = new URL(sitio + "Canciones/actualizar/" + nombreCancion);

		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		try {
			http.setRequestMethod("PUT");
			http.setDoOutput(true);
			http.setRequestProperty("Content-Type", "application/json");

			// Construcción del objeto JSON para la actualización de la canción
			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put("nombreCancion", nombreCancion);
			jsonRequest.put("nombreArtista", nuevaCancion.getNombreArtista());
			jsonRequest.put("generoMusical", nuevaCancion.getGeneroMusical());
			jsonRequest.put("rutaDelArchivo", nuevaCancion.getRutaDelArchivo());
			jsonRequest.put("emisora", nuevaCancion.getNombreEmisora());

			// Imprimir estado en la consola
			System.out.println("Enviando solicitud de actualización...");

			OutputStream outputStream = http.getOutputStream();
			outputStream.write(jsonRequest.toString().getBytes(StandardCharsets.UTF_8));
			outputStream.flush();

			int respuesta = http.getResponseCode();

			// Imprimir estado en la consola
			System.out.println("Respuesta del servidor: " + respuesta);

			return respuesta;
		} finally {
			http.disconnect();
		}
	}

	public static int eliminarCancion(String nombreCancion) throws IOException {
		URL url = new URL(sitio + "Canciones/eliminar/" + nombreCancion);

		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		try {
			http.setRequestMethod("DELETE");
			http.setDoOutput(true);
			http.setRequestProperty("Content-Type", "application/json");

			// JSON para eliminar por nombre de la canción
			JSONObject jsonRequest = new JSONObject();
			jsonRequest.put("nombreCancion", nombreCancion); // Nombre de la canción a eliminar

			// Imprimir estado en la consola
			System.out.println("Enviando solicitud de eliminación...");

			OutputStream outputStream = http.getOutputStream();
			outputStream.write(jsonRequest.toString().getBytes(StandardCharsets.UTF_8));
			outputStream.flush();

			int respuesta = http.getResponseCode();

			// Imprimir estado en la consola
			System.out.println("Respuesta del servidor: " + respuesta);

			return respuesta;
		} finally {
			http.disconnect();
		}
	}

	public static List<CancionDTO> listarCanciones() throws IOException {
		URL url = new URL(sitio + "Canciones/listar");

		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		try {
			http.setRequestMethod("GET");
			http.setRequestProperty("Accept", "application/json");

			InputStream respuesta = http.getInputStream();
			byte[] inp = respuesta.readAllBytes();
			String json = new String(inp, StandardCharsets.UTF_8);

			List<CancionDTO> canciones = parsearListaCanciones(json);
			return canciones;
		} finally {
			http.disconnect();
		}
	}

	private static List<CancionDTO> parsearListaCanciones(String json) {
		List<CancionDTO> canciones = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < ((CharSequence) jsonArray).length(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			CancionDTO cancion = new CancionDTO();

			cancion.setNombreCancion(jsonObject.getString("nombreCancion"));
			cancion.setNombreArtista(jsonObject.getString("nombreArtista"));
			cancion.setGeneroMusical(jsonObject.getString("generoMusical"));
			cancion.setRutaDelArchivo(jsonObject.getString("rutaDelArchivo"));
			cancion.setNombreEmisora(jsonObject.getString("nombreEmisora"));

			canciones.add(cancion);
		}

		return canciones;
	}

}
