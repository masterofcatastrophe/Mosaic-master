package codesages.mosaic.helpers;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by DELL on 10/11/2016.
 */

public class ContactManager {

    public static ArrayList<Contact> ReadPhoneContacts(Context cntx) //This Context parameter is nothing but your Activity class's Context
    {
        ArrayList<Contact> contactsList = new ArrayList<>();
        Cursor cursor = cntx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        Integer contactsCount = cursor.getCount(); // get how many contacts you have in your contacts list
        if (contactsCount > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Contact contact = new Contact(contactName, id);
                Log.d("ContactManager", "ContactName: " + contactName + " Id: " + id);
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    //the below cursor will give you details for multiple contacts
                    Cursor pCursor = cntx.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);

                    Cursor emailCur = cntx.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);

                    ContactNumbers numbers = new ContactNumbers();
                    ArrayList<String> numbersArray = new ArrayList<>();
                    ArrayList<String> emailsArray = new ArrayList<>();

                    while (pCursor.moveToNext()) {
                        int phoneType = pCursor.getInt(
                                pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo = pCursor.getString(
                                pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        numbersArray.add(phoneNo);
                    }
                    pCursor.close();

                    while (emailCur.moveToNext()) {
                        // This would allow you get several email addresses
                        // if the email addresses were stored in an array
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                        emailsArray.add(email);
                    }
                    emailCur.close();

                    if (numbersArray.size() > 0) {
                        numbers.setNumbers(numbersArray);
                        numbers.setEmails(emailsArray);
                        contact.setNumbers(numbers);
                        contactsList.add(contact);
                    }

                }
            }
            cursor.close();
        }
        Collections.sort(contactsList, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return contactsList;
    }

    public static void getPhoneType(int phoneType) {
        switch (phoneType) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                Log.e(contactName + ": TYPE_MOBILE", " " + phoneNo);
//                numbers.setPhone(phoneNo);
//                numbersArray.add(phoneNo);
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                Log.e(contactName + ": TYPE_HOME", " " + phoneNo);
//                numbers.setHome(phoneNo);
//                numbersArray.add(phoneNo);
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                Log.e(contactName + ": TYPE_WORK", " " + phoneNo);
//                numbers.setWork(phoneNo);
//                numbersArray.add(phoneNo);
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
//                Log.e(contactName + ": TYPE_WORK_MOBILE", " " + phoneNo);
//                numbersArray.add(phoneNo);
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
//                Log.e(contactName + ": TYPE_OTHER", " " + phoneNo);
//                numbersArray.add(phoneNo);
                break;
            default:
                break;
        }
    }
}
