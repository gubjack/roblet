// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

/**
 * This module is a client library to run roblets on servers.
 * 
 * @author Hagen Stanek
 */
module  roblet.client
{
    exports org.roblet.client;

    requires transitive roblet.root;
    requires roblet.protocol;
    requires roblet.server.unit;
}
