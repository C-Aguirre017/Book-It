package proyecto.proyectobookit.utils;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import proyecto.proyectobookit.base_datos.Pin;

/**
 * Created by carlos on 22-08-15.
 */
public class AlertDialogMetodos {

    //Info Pin
    public static String crearInfoPin(String Descripcion, String Publicacion, String precio,String Tipo_ayuda){
        String Mensaje="Descripcion: \n";
        if (Descripcion != null && !Descripcion.equals(""))
            Mensaje += "\t " + Descripcion;
        else
            Mensaje += "\t Sin Información";

        Mensaje+= "\n Publicado el: \n";
        if (Publicacion != null && !Publicacion.equals("") ) {
            Mensaje += "\t " + Publicacion;
        } else {
            Mensaje += "\t Sin Información";
        }

        Mensaje+= "\n Dispuesto a Pagar: \n";
        if (precio != null && !precio.equals("") ) {
            Mensaje += "\t " + precio;
        } else {
            Mensaje += "\t Sin Información";
        }

        Mensaje+= "\n Tipo de Ayuda: \n";
        if (Tipo_ayuda != null && !Tipo_ayuda.equals("") ) {
            Mensaje += "\t " + Tipo_ayuda;
        } else {
            Mensaje += "\t Sin Información";
        }

        return Mensaje;
    }

    private static String crearInfoPin(Pin aux) {
        String Mensaje="Descripcion: \n";
        if (aux.getDescripcion() != null && !aux.getDescripcion().equals(""))
            Mensaje += "\t " +aux.getDescripcion();
        else
            Mensaje += "\t Sin Información";

        Mensaje+= "\n Fecha: \n";
        if (aux.getHora() != null && !aux.getHora().equals("") ) {
            String[] tiem = aux.getHora().split(" ");

            Mensaje += "\t " + tiem[0]  + "\n Hora: \n \t " + tiem[1].substring(0,5);
        } else {
            Mensaje += "\t Sin Información";
        }

        Mensaje+= "\n Dispuesto a Pagar: \n";
        if (aux.getPrecio() != null && !aux.getPrecio().equals("") ) {
            Mensaje += "\t " + aux.getPrecio();
        } else {
            Mensaje += "\t Sin Información";
        }

        Mensaje+= "\n Facultad: \n";
        if (aux.getCampus() != null && !aux.getCampus().equals("") ) {
            Mensaje += "\t " + aux.getCampus();
        } else {
            Mensaje += "\t Sin Información";
        }

        return Mensaje;
    }

    //Alert Dialogs
    public static void crearAlertPin(final Pin Pin_Elegido, final Context mContext) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(Pin_Elegido.getRamo_Pin().getSigla() + " " + Pin_Elegido.getRamo_Pin().getNombre())
                    .setMessage(crearInfoPin(Pin_Elegido))
                    .setPositiveButton("Mail", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String[] TO = {Pin_Elegido.getUsuario_Pin().getEmail()};
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");

                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, Configuracion.NOMBRE + ": Aceptar " + Pin_Elegido.getRamo_Pin().getNombre());
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Me gustaría realizar la clase que publicaste en " + Configuracion.NOMBRE + "  \n " +
                                    "Para Contactarme te envío mi telefono. \n" +
                                    "Tel: " + "\n Saludos");
                            try {
                                mContext.startActivity(Intent.createChooser(emailIntent, "Elija un cliente de correo electrónico: "));
                                Log.i("Email enviado", "");
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(mContext, "No hay cliente de correo electónico instalado!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNeutralButton("Whatsapp", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp",mContext);
                            if (isWhatsappInstalled) {
                                Boolean ContactoCreado = createContactBukit(Configuracion.NOMBRE + " / " + Pin_Elegido.getUsuario_Pin().getNombre(), Pin_Elegido.getUsuario_Pin().getTelefono(),mContext);
                                if (ContactoCreado) {
                                    Uri uri = Uri.parse("smsto:" + Pin_Elegido.getUsuario_Pin().getTelefono());
                                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                                    i.setPackage("com.whatsapp");
                                    mContext.startActivity(Intent.createChooser(i, ""));
                                    //deleteContact(numero,nombre);
                                }
                            } else {
                                Toast.makeText(mContext, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                mContext.startActivity(goToMarket);
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }catch (Exception e){
            Toast.makeText(mContext,"Error",Toast.LENGTH_LONG);
        }
    }


    //Metodos Privados
    private static boolean whatsappInstalledOrNot(String uri,Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private static Boolean createContactBukit(String nombre, String numero,Context mContext) {
        String displayName = nombre;
        String mobileNumber = numero;
        String email = null;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        // Names
        if (displayName != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            displayName).build());
        }

        // Mobile Number
        if (mobileNumber != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
        }

        // Email
        if (email != null) {
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                            ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());
        }

        // Asking the Contact provider to create a new contact
        try {
            mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteContact(String phone, String name ,Context mContext) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = mContext.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        mContext.getContentResolver().delete(uri, null, null);
                        return true;
                    }
                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return false;
    }

}
