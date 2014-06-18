package com.itjiehun.bootmanager.shell;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;

import android.content.ComponentName;
import android.util.Log;

public class ShellCommand {
	private static final String TAG = "ShellCommand";
	private Boolean can_su;
	public SH sh = new SH("sh");
	public SH su = new SH("su");

	public boolean canSU() {
		return canSU(false);
	}

	public boolean canSU(boolean paramBoolean) {
		if ((can_su == null) || (paramBoolean)) {
			if (!checkSuAvailable()) {
				can_su = false;
				return can_su;
			}
			CommandResult r = su.runWaitFor("id");
			StringBuilder out = new StringBuilder();
			if (r.stdout != null) {
				out.append(r.stdout).append(" ; ");
			}
			if (r.stderr != null) {
				out.append(r.stderr);
			}
			can_su = r.success();
		}
		return can_su;
	}

	public boolean checkSuAvailable() {
		if (new File("/system/bin/su").exists()) {
			return true;
		}
		if (new File("/system/xbin/su").exists()) {
			return true;
		}
		if (new File("/data/bin/su").exists()) {
			return true;
		}
		if (new File("/sbin/su").exists()) {
			return true;
		}
		if (new File("/vendor/bin/su").exists()) {
			return true;
		}
		if (new File("/system/sbin/su").exists()) {
			return true;
		}
		return false;
	}

	public CommandResult setComponentSetting(ComponentName paramComponentName, boolean paramBoolean) {
		SH localSH = this.su;
		String str = "pm ";
		if (paramBoolean) {
			str += "enable ";
		} else {
			str += "disable ";
		}
		return localSH.runWaitFor(str + paramComponentName.flattenToString());
	}

	public SH suOrSH() {
		if (canSU()) {
			return this.su;
		}
		return this.sh;
	}

	public class CommandResult {
		public final String stdout;
		public final String stderr;
		public final Integer exit_value;

		CommandResult(Integer localInteger) {
			this(localInteger, null, null);
		}

		CommandResult(Integer exit_value_in, String stdout_in, String stderr_in) {
			exit_value = exit_value_in;
			stdout = stdout_in;
			stderr = stderr_in;
		}

		public boolean success() {
			return exit_value != null && exit_value == 0;
		}

		public String toString() {
			return "CommandResult [stdout=" + this.stdout + ", stderr=" + this.stderr + ", exit_value="
					+ this.exit_value + "]";
		}
	}

	public class SH {
		private String SHELL = "sh";

		public SH(String SHELL_in) {
			SHELL = SHELL_in;
		}

		private String getStreamLines(InputStream is) {
			String out = null;
			StringBuffer buffer = null;
			DataInputStream dis = new DataInputStream(is);
			try {
				if (dis.available() > 0) {
					buffer = new StringBuffer(dis.readLine());
					while (dis.available() > 0)
						buffer.append("\n").append(dis.readLine());
				}
				dis.close();
			} catch (Exception ex) {
			}
			if (buffer != null)
				out = buffer.toString();
			return out;
		}

		public Process run(String s) {
			Process process = null;
			try {
				process = Runtime.getRuntime().exec(SHELL);
				DataOutputStream toProcess = new DataOutputStream(process.getOutputStream());
				toProcess.writeBytes("exec " + s + "\n");
				toProcess.flush();
			} catch (Exception e) {
				process = null;
				Log.e("run", "Exception while trying to run: '" + s + "' " + e.getMessage());
			}
			return process;
		}

		public CommandResult runWaitFor(String s) {
			Process process = run(s);
			Integer exit_value = null;
			String stdout = null;
			String stderr = null;
			if (process != null) {
				try {
					exit_value = process.waitFor();
					stdout = getStreamLines(process.getInputStream());
					stderr = getStreamLines(process.getErrorStream());
				} catch (InterruptedException e) {
				} catch (NullPointerException e) {
				}
			}
			return new CommandResult(exit_value, stdout, stderr);
		}
	}
}