// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.client;


class  NoLogger
    extends Logger
{

    public void  base       (Object caller, Object message) {}
    public void  call       (Object caller, Object message) {}
    public void  confirm    (Object caller, Object message) {}
    public void  connect    (Object caller, Object message) {}
    public void  link       (Object caller, Object message) {}
    public void  queues     (Object caller, Object message) {}
    public void  transport  (Object caller, Object message) {}
    public void  tube       (Object caller, Object message) {}

}
