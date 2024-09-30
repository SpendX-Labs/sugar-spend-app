/** @type {import('next').NextConfig} */
const nextConfig = {
  eslint: {
    ignoreDuringBuilds: process.env.NEXT_PUBLIC_ESLINT_DISABLED === "true",
  },
};

export default nextConfig;
