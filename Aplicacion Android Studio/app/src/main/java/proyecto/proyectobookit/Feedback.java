package proyecto.proyectobookit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Feedback extends Activity {

    Usuario Mi_Usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Mi_Usuario = new Usuario();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Mi_Usuario.setEmail(extras.getString("email"));
            Mi_Usuario.setNombre(extras.getString("nombre"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.feedbackmenu_send) {
            EditText texto = (EditText) findViewById(R.id.feedback_texto);
            if (texto.getText().toString().length()>=0)
                sendMail("bookit.servidor@gmail.com","Feedback",texto.getText().toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Enviar(View v){
        EditText texto = (EditText) findViewById(R.id.feedback_texto);
        if (texto.getText().toString().length()>=0)
            sendMail("bookit.servidor@gmail.com","Feedback",texto.getText().toString());
    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();
        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Feedback.this, "Espere porfavor", "Enviando correo electronico", true, false);
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            CrearAlertDialog("Muchas gracias","Feedback");
        }

    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Mi_Usuario.getEmail(), Mi_Usuario.getNombre() + " " + Mi_Usuario.getEmail()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getResources().getString(R.string.mailaplicacion),getResources().getString(R.string.clave_mail_aplicacion));
            }
        });
    }

    private void CrearAlertDialog(String Mensaje, String Titulo){

        AlertDialog.Builder builder = new AlertDialog.Builder(Feedback.this);
        builder.setTitle(Titulo)
                .setMessage(Mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
