// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

/**
 * This module contains every thing needed to provide (roblet server) modules
 * suitable to be loaded by the roblet server.
 *
 * @author Hagen Stanek
 */
module  roblet.server.module
{
    requires transitive roblet.root;

    exports genRob.genControl.modules;
}
