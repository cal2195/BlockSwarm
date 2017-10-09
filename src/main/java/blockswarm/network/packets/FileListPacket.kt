package blockswarm.network.packets

import blockswarm.database.entries.FileEntry
import java.io.Serializable
import java.util.ArrayList

/**
 *
 * @author cal
 */
class FileListPacket(val files: ArrayList<FileEntry>) : Serializable
