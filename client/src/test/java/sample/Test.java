// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package sample;
import java.io.Serializable;
import java.time.Instant;
import org.roblet.Roblet;
import org.roblet.client.Slot;

/**
 * @author Hagen Stanek
 */
public class  Test  implements Roblet<Instant>, Serializable
{
    public static void  main (String[] args) throws InterruptedException
    {
        try (Slot slot = new Slot (null)) {
            System.out.println (slot. run (new Test ()));
        }
    }
    public Instant  execute (org.roblet.Robot robot)
    {
        return Instant. now ();
    }
}
