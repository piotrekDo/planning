package com.piotrdomagalski.planning.mailing;


import java.time.LocalDateTime;

/**
 * A generic template for e-mail messages sent to users.
 */

public abstract class EmailTemplate {

    abstract String setMessage();

    private String greet() {
        if (LocalDateTime.now().getHour() > 18)
            return "Good afternoon";
        else
            return "Good morning";
    }

    public String create(String reciverName) {
        return String.format("""
                                    
                        <html>
                        <head>
                           <title>Notification</title>      \s
                        </head>
                        <style>
                            .myClass {
                                text-decoration:underline;
                                   color:blue;
                            }
                           </style>
                        <body>     \s
                            <h1 style="color:red;">%s, %s!</h1>
                            <section class="myClass">%s</section>
                            </br>        
                            </br>
                            <p>Regards, Planning team</p>        
                           </body>
                        </html>
                        """,
                greet()
                , reciverName
                , setMessage());
    }

}
