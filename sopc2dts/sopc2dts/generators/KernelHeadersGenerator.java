package sopc2dts.generators;

import sopc2dts.lib.Parameter;
import sopc2dts.lib.AvalonSystem;
import sopc2dts.lib.BoardInfo;
import sopc2dts.lib.components.BasicComponent;

public class KernelHeadersGenerator extends AbstractSopcGenerator {
	boolean minimal = true;
	public KernelHeadersGenerator(AvalonSystem s) {
		super(s);
	}

	@Override
	public String getExtension() {
		return "h";
	}

	@Override
	public String getOutput(BoardInfo bi) {
		String res = null;
		for(BasicComponent comp : sys.getSystemComponents())
		{
			if((!minimal) || comp.getScd().getGroup().equalsIgnoreCase("cpu"))
			{
				//Only dump nios stuff if REALLY needed
				if(res==null)
				{
					res = copyRightNotice + 
						"#ifndef _ALTERA_CPU_H_\n" +
						"#define _ALTERA_CPU_H_\n\n";
				}
				res += "/*\n" +
						" * Dumping parameters for " + comp.getInstanceName() + " (type " + comp.getScd().getGroup() + ")\n" +
						" * This is not as clean as I hoped but FDT is just a tad late\n" +
						" */\n";
				for(Parameter ass : comp.getParams())
				{
					if(ass.getName().toUpperCase().startsWith("EMBEDDEDSW.CMACRO."))
					{
						res += "#define " + ass.getName().substring(18).toUpperCase() + "\t" + ass.getValue() + "\n";
					}
				}
			}
		}
		if(res!=null)
		{
			res += "\n#endif //_ALTERA_CPU_H_\n";
		}
		return res;
	}

}
